package ui;

import exception.ResponseException;
import model.GameData;
import request.JoinGameRequest;
import response.GameResponse;
import web.ChessClient;

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
//                return help();
                return observeGame("22025");
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
            throw new ResponseException(400, "Expected: create <GAME ID>");
        }
        int gameID = ChessClient.getClient().getServer().createGame(new GameData(1, null, null, params[0], null));

        return String.format("Game '%s' created with ID: %s", params[0], gameID);
    }

    private String listGames(String ...params) throws ResponseException {
        return ChessClient.getClient().getServer().listGames();
    }

    private String joinGame(String ...params) throws ResponseException {
        if(params.length != 2){
            throw new ResponseException(400, "Expected: join <GAME ID> <WHITE|BLACK|empty>");
        }
        ChessClient.getClient().getServer().joinGame(new JoinGameRequest(params[1].toUpperCase(), Integer.parseInt(params[0])));

        ChessClient.getClient().setState(State.GAMEPLAY);
        return String.format("%s joined game %s", ChessClient.getClient().getUser().username(), params[0]);
    }

    private String observeGame(String ...params) throws ResponseException {
        if (params.length != 1){
            throw new ResponseException(400, "Expected: observe <GAME ID>");
        }
        ChessClient.getClient().getServer().joinGame(new JoinGameRequest(null, Integer.parseInt(params[0])));

        ChessClient.getClient().setState(State.GAMEPLAY);
        return String.format("%s is observing game %s", ChessClient.getClient().getUser().username(), params[0]);
    }


}
