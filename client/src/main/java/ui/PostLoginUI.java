package ui;

import exception.ResponseException;
import model.GameData;
import request.JoinGameRequest;
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
        if(params.length != 2){
            // Why the ambiguity for rejoin?
            throw new ResponseException(400, "Expected: join <GAME ID> <WHITE|BLACK>");
        }

        int displayedID = Integer.parseInt(params[0]);
        Map<Integer, GameData> currentGames = ChessClient.getClient().getCurrentGames();
        GameData foundGame = currentGames.get(displayedID);
        String color = params[1].toUpperCase();

        ChessClient.getClient().getServer().joinGame(new JoinGameRequest(color, foundGame.gameID()));

        ChessClient.getClient().setGameID(foundGame.gameID());
        ChessClient.getClient().setCurrentGameData(foundGame);
        ChessClient.getClient().setPlayerColor(color);

        try{
            ChessClient.getClient().getWebSocketFacade().joinPlayer();
        } catch (IOException e){
            throw new ResponseException(500, e.getMessage());
        }

        ChessClient.getClient().setState(State.GAMEPLAY);
        String joiningPlayerUsername = ChessClient.getClient().getUser().username();

        return String.format("%s joined game %s", joiningPlayerUsername, foundGame.gameName());
    }

    private String observeGame(String ...params) throws ResponseException {
        if (params.length != 1){
            throw new ResponseException(400, "Expected: observe <GAME ID>");
        }
        Map<Integer, GameData> currentGames = ChessClient.getClient().getCurrentGames();
        GameData foundGame = currentGames.get(Integer.parseInt(params[0]));

        ChessClient.getClient().setGameID(foundGame.gameID());
        ChessClient.getClient().setCurrentGameData(foundGame);
        // temp
        ChessClient.getClient().setPlayerColor("WHITE");
        ChessClient.getClient().getServer().joinGame(new JoinGameRequest(null, foundGame.gameID()));

        try{
            ChessClient.getClient().getWebSocketFacade().joinObserver();
        } catch (IOException e){
            throw new ResponseException(500, e.getMessage());
        }

        ChessClient.getClient().setState(State.GAMEPLAY);
        return String.format("%s is observing game %s", ChessClient.getClient().getUser().username(), foundGame.gameName());
    }


}
