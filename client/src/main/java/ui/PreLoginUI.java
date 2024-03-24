package ui;


import model.UserData;
import web.ChessClient;

public class PreLoginUI implements UserInterface{

    public String eval(String cmd, String[] params){
        switch(cmd){
            case "help" -> {
                return help();
            }
            case "quit" -> {
//                return quit();
            }
            case "login" -> {
//                return login();
            }
            case "register" -> {
//                return register();
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
        Commands:
        Help: help
        Quit: quit
        Login: login <USERNAME> <PASSWORD>
        Register: register <USERNAME> <PASSWORD> <EMAIL>
        """;

        return message;
    }

    private String login(String... params){
        if(params.length != 2){
//            throw new ResponseException(400, "Expected: login <USERNAME> <PASSWORD>");
        }
//        ChessClient.getClient().getServer().login(new UserData(params[0], params[1], null));
        ChessClient.getClient().setState(State.LOGGEDIN);
        return String.format("Logged in as %s.", params[0]);
    }
}
