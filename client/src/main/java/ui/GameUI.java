package ui;

import chess.ChessGame;
import exception.ResponseException;
import web.ChessClient;
import web.websocket.GameHandler;
import web.websocket.NotificationHandler;
import web.websocket.WebSocketFacade;

public class GameUI implements UserInterface {

    public String eval(String cmd, String[] params){
        switch(cmd){
            case "redraw" -> {
                return redraw();
            }
            case "leave" -> {
                return leave();
            }
            case "makemove" -> {
                return help();
            }
            case "resign" -> {
                return help();
            }
            case "highlight" -> {
                return help();
            }
            default -> {
                return help();
            }
        }
    }

    private String help() {
        String message =
        """
        Redraw chess board: 'redraw'
        Leave game: 'leave'
        Make a move: 'make move <piece location> <end location>'
        Resign from game: 'resign'
        Highlight legal moves: 'highlight'
        Help: 'help'
        """;

        return message;
    }

    private String redraw(){
        return ChessBoardPrinter.printBoard(ChessClient.getClient().getCurrentGame().game(), ChessClient.getClient().getPlayerColor());
    }

    private String leave(){
        ChessClient.getClient().getWebSocketFacade().leaveGame();
        ChessClient.getClient().setCurrentGame(null);
        ChessClient.getClient().setState(State.LOGGEDIN);

        return String.format("%s left the game.", ChessClient.getClient().getUser().username());
    }

    private String makeMove(){

    }










}
