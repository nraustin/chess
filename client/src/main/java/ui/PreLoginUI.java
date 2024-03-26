package ui;


import exception.ResponseException;
import model.UserData;
import web.ChessClient;

public class PreLoginUI implements UserInterface{

    public String eval(String cmd, String[] params) throws ResponseException {
        switch(cmd){
            case "quit" -> {
                return "Goodbye!";
            }
            case "login" -> {
                return login(params);
            }
            case "register" -> {
                return register(params);
            }
            default -> {
                return help();
            }
        }
    }

    private String help() {
        String message =
        """
        Login: 'login <USERNAME> <PASSWORD>'
        Register: 'register <USERNAME> <PASSWORD> <EMAIL>'
        Quit: 'quit'
        Help: 'help'
        """;

        return message;
    }

    private String login(String... params) throws ResponseException {
        if(params.length != 2){
            throw new ResponseException(400, "Expected: login <USERNAME> <PASSWORD>");
        }
        UserData userData = new UserData(params[0], params[1], null);
        ChessClient.getClient().getServer().login(userData);

        ChessClient.getClient().setUser(userData);
        ChessClient.getClient().setState(State.LOGGEDIN);
        return String.format("Logged in as %s", params[0]);
    }

    private String register(String ...params) throws ResponseException {
        if(params.length != 3){
            throw new ResponseException(400, "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
        }
        UserData userData = new UserData(params[0], params[1], params[2]);
        ChessClient.getClient().getServer().register(userData);

        ChessClient.getClient().setUser(userData);
        ChessClient.getClient().setState(State.LOGGEDIN);
        return String.format("%s registered", params[0]);
    }
}
