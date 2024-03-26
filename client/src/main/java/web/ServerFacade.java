package web;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;
import response.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import exception.ResponseException;
import ui.EscapeSequences;

public class ServerFacade {

    private final String serverURL;
    private String authToken = null;

    public ServerFacade(String serverURL){
        this.serverURL = serverURL;
    }

    public String getAuthToken(){
        return authToken;
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public void login(UserData userData) throws ResponseException {
        AuthData authData = httpHandler("POST", "/session", userData, AuthData.class);
        authToken = authData.authToken();
    }

    public void register(UserData userData) throws ResponseException {
        AuthData authData = httpHandler("POST", "/user", userData, AuthData.class);
        authToken = authData.authToken();
    }

    public void logout() throws ResponseException {
        httpHandler("DELETE", "/session", null, null);
        authToken = null;
    }

    public int createGame(GameData gameData) throws ResponseException {
        GameResponse gameRes = httpHandler("POST", "/game", gameData, GameResponse.class);
        ChessClient.getClient().addGame(gameRes.getGame());

        return gameRes.getGameID();
    }

    public String listGames() throws ResponseException {
        GameResponse gameRes = httpHandler("GET", "/game", null, GameResponse.class);
        assignGameIndexes(gameRes);

        return gamesToString();
    }

    public void joinGame(JoinGameRequest joinRequest) throws ResponseException {
        httpHandler("PUT", "/game", joinRequest, null);
    }

    public void clearData() throws ResponseException {
        httpHandler("DELETE", "/db", null, null);
    }


    private String gamesToString(){
        StringBuilder s = new StringBuilder();
        Map<Integer, GameData> gameMap = ChessClient.getClient().getCurrentGames();

        if(gameMap.isEmpty()){
            return "No current games.";
        }
        s.append("ALL GAMES:\n");
        for(Map.Entry<Integer, GameData> entry : gameMap.entrySet()){
            Integer key = entry.getKey();
            GameData game = entry.getValue();
            String gameListing =
                String.format(
                    """
                    \n %s
                    %s Players (%swhite, %sblack%s): %s, %s
                    %s Game ID: %s
                    """,
                    EscapeSequences.SET_TEXT_COLOR_BLUE + game.gameName(),
                    EscapeSequences.SET_TEXT_COLOR_GREEN, EscapeSequences.SET_TEXT_COLOR_WHITE, EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY, EscapeSequences.SET_TEXT_COLOR_GREEN,
                    (EscapeSequences.SET_TEXT_COLOR_WHITE + (game.whiteUsername() == null ? EscapeSequences.SET_TEXT_COLOR_GREEN + "No player" : game.whiteUsername())),
                    (EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + (game.blackUsername() == null ? EscapeSequences.SET_TEXT_COLOR_GREEN + "No player" : game.blackUsername())),
                    EscapeSequences.SET_TEXT_COLOR_BLUE, key);
            s.append(gameListing);
        }

        return s.toString();
    }

    private void assignGameIndexes(GameResponse gameRes){
        Map<Integer, GameData> gameIndexes = new HashMap<>();
        HashSet<GameData> games = gameRes.getGames();

        int i = 1;
        for(GameData game: games){
            gameIndexes.put(i, game);
            i++;
        }
        ChessClient.getClient().setCurrentGames(gameIndexes);
    }


    private <T> T httpHandler(String method, String endpoint, Object request, Class<T> resClass) throws ResponseException {
        try {
            URL url = new URI(serverURL + endpoint).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(request != null);

            if (authToken != null) {
                connection.addRequestProperty("Authorization", authToken);
            }

            if (request != null) {
                connection.addRequestProperty("Content-Type", "application/json");
                OutputStream os = connection.getOutputStream();
                writeReqBody(os, request);
            }

            connection.connect();

            int status = connection.getResponseCode();
            if (status != 200) {
                throw new ResponseException(status, String.format("Failure: %s: %s", status, connection.getResponseMessage()));
            }

            return readResBody(connection, resClass);
        } catch (ResponseException e){
            throw new ResponseException(e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void writeReqBody(OutputStream os, Object request) throws IOException {
        String reqData = new Gson().toJson(request);
        os.write(reqData.getBytes());
    }

    private static <T> T readResBody (HttpURLConnection connection, Class<T> resClass) throws IOException {
        T res = null;
        if(connection.getContentLength() < 0){
            try (InputStream resBody = connection.getInputStream()){
                InputStreamReader reader = new InputStreamReader(resBody);
                if(resClass != null) {
                    res = new Gson().fromJson(reader, resClass);
                }
            }
        }
        return res;
    }


}
