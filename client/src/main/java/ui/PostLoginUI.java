package ui;

import exception.ResponseException;
import model.GameData;
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
//                return listGames();
            }
            case "join" -> {
//                return joinGame();
            }
            case "observe" -> {
//                return observeGame();
            }
            default -> {
                return help();
            }
        }
        return "not implemented";
    }

    private String help() {
        String message =
        """
        Create a game: 'create <GAME NAME>'
        Show games: 'list'
        Join a game: 'join <GAME ID>'
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
        ChessClient.getClient().getServer().createGame(new GameData(1, null, null, params[0], null));

        return String.format("Game '%s' created", params[0]);
    }


}
