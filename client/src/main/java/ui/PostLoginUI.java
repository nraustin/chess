package ui;

public class PostLoginUI implements UserInterface {

    public String eval(String cmd, String[] params) {
        switch(cmd){
            case "quit" -> {
                return "Goodbye!";
            }
            case "logout" -> {
//                return logout();
            }
            case "create" -> {
//                return createGame(params);
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
        Show all games: 'list'
        Join a game: 'join <GAME ID>'
        Observe a game: 'observe <GAME ID>'
        Logout: 'logout'
        Quit: 'quit'
        Help: 'help'
        """;

        return message;
    }
}
