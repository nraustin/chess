package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import request.JoinGameRequest;
import response.GameResponse;
import web.ChessClient;

import java.io.IOException;
import java.util.Map;

public class PostLoginUI implements UserInterface {

    public String eval(String cmd, String[] params) throws ResponseException {
        switch(cmd){
            case "quit" -> {
                return "Goodbye!";
            }
            case "logout" -> {
                return logout();
            }
            case "create" -> {
                return createGame(params);
            }
            case "list" -> {
                return listGames();
            }
            case "join" -> {
                return joinGame(params);
            }
            case "observe" -> {
                return observeGame(params);
            }
            default -> {
                return help();
            }
        }
    }

    private String help() {
        String message =
        """
        Create a game: 'create <GAME NAME>'
        Show games: 'list'
        Join a game: 'join <GAME ID> <WHITE|BLACK|empty>'
        Observe a game: 'observe <GAME ID>'
        Logout: 'logout'
        Quit: 'quit'
        Help: 'help'
        """;

        return message;
    }

    private String logout() throws ResponseException {
        ChessClient.getClient().getServer().logout();
        ChessClient.getClient().setState(State.LOGGEDOUT);

        String username = ChessClient.getClient().getUser().username();
        ChessClient.getClient().clearUser();
        return String.format("%s logged out", username);
    }

    private String createGame(String ...params) throws ResponseException {
        if(params.length != 1){
            throw new ResponseException(400, "Expected: create <GAME NAME>");
        }
        ChessClient.getClient().getServer().createGame(new GameData(1, null, null, params[0], null));

        return String.format("Game '%s' created", params[0]);
    }

    private String listGames(String ...params) throws ResponseException {
        return ChessClient.getClient().getServer().listGames();
    }

    private String joinGame(String ...params) throws ResponseException {
        if(params.length == 0){
            throw new ResponseException(400, "Expected: join <GAME ID> <WHITE|BLACK|empty>");
        }
        Map<Integer, GameData> currentGames = ChessClient.getClient().getCurrentGames();
        GameData gameData  = currentGames.get(Integer.parseInt(params[0]));

        String color = params.length == 1 ? null : params[1].toUpperCase();

        String whitePlayerUsername = gameData.whiteUsername();
        String blackPlayerUsername = gameData.blackUsername();
        String joiningPlayerUsername = ChessClient.getClient().getUser().username();

        boolean returningPlayer = color == null && joiningPlayerUsername.equals(whitePlayerUsername) || joiningPlayerUsername.equals(blackPlayerUsername);
        boolean newPlayer = color != null;
        boolean observer = !returningPlayer && !newPlayer;

        ChessClient.getClient().setCurrentGame(gameData);
        ChessClient.getClient().setPlayerColor(color);

        ChessClient.getClient().getServer().joinGame(new JoinGameRequest(color, gameData.gameID()));

        try{
            if(!observer){
                ChessClient.getClient().getWebSocketFacade().joinPlayer();
            } else{
                ChessClient.getClient().getWebSocketFacade().joinObserver();
            }
        } catch (IOException e){
            throw new ResponseException(500, e.getMessage());
        }

        ChessClient.getClient().setState(State.GAMEPLAY);

        if(observer){
            return String.format(
                    """
                    %s is observing game %s
                    (hint): to join a game, select a color to join as.
                    """, joiningPlayerUsername, gameData.gameName());
        }
        return String.format("%s joined game %s", joiningPlayerUsername, params[0]);
    }

    private String observeGame(String ...params) throws ResponseException {
        if (params.length != 1){
            throw new ResponseException(400, "Expected: observe <GAME ID>");
        }
        Map<Integer, GameData> currentGames = ChessClient.getClient().getCurrentGames();
        Integer gameID = currentGames.get(Integer.parseInt(params[0])).gameID();
        String gameName = currentGames.get(Integer.parseInt(params[0])).gameName();

        ChessClient.getClient().getServer().joinGame(new JoinGameRequest(null, gameID));

        ChessClient.getClient().setState(State.GAMEPLAY);
        return String.format("%s is observing game '%s'", ChessClient.getClient().getUser().username(), gameName);
    }


}
