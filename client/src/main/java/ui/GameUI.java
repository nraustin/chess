package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import web.ChessClient;
import web.websocket.GameHandler;
import web.websocket.NotificationHandler;
import web.websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Scanner;

public class GameUI implements UserInterface {

    public String eval(String cmd, String[] params) throws ResponseException {
        switch(cmd){
            case "redraw" -> {
                return redraw();
            }
            case "leave" -> {
                return leave();
            }
            case "makemove" -> {
                return makeMove(params);
            }
            case "resign" -> {
                return resign();
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
        Make a move: 'makemove <piece location> <end location> <promotion>'
                      Example: 'a1 c4 k'
        Resign from game: 'resign'
        Highlight legal moves: 'highlight'
        Help: 'help'
        """;

        return message;
    }

    private String redraw(){
        return ChessBoardPrinter.printBoard(ChessClient.getClient().getCurrentGame().game(), ChessClient.getClient().getPlayerColor());
    }

    private String leave() throws ResponseException {
        try{
            ChessClient.getClient().getWebSocketFacade().leaveGame();
        } catch (IOException e){
            throw new ResponseException(500, "Error:" + e.getMessage());
        }
        ChessClient.getClient().setCurrentGame(null);
        ChessClient.getClient().setState(State.LOGGEDIN);

        return String.format("%s left the game.", ChessClient.getClient().getUser().username());
    }

    private String resign() throws ResponseException {
        System.out.print("Are you sure? You will forfeit the game. (y/n) ");
        Scanner scanner = new Scanner(System.in);
        String res = scanner.next();
        if(res.equals("y")) {
            try {
                ChessClient.getClient().getWebSocketFacade().resignGame();
            } catch (IOException e) {
                throw new ResponseException(500, "Error:" + e.getMessage());
            }
//            ChessClient.getClient().setCurrentGame(null);
//            ChessClient.getClient().setState(State.LOGGEDIN);
        }
        return "";

    }

    private String makeMove(String ...params) throws ResponseException {
        if(params.length != 2 && params.length != 3){
            throw new ResponseException(400, "Expected: makemove <piece location> <end location> <promotion>\n Example: 'a1 c4 k'");
        }
        if(params[0].length() != 2 || params[1].length() !=2){
            throw new ResponseException(400, "Error: invalid start or end position");
        }
        ChessPosition startPos = parsePosition(params[0]);
        ChessPosition endPos = parsePosition(params[1]);

        GameData currentGame = ChessClient.getClient().getCurrentGame();
        ChessGame.TeamColor currentColor = ChessClient.getClient().getPlayerColor();
        ChessPiece.PieceType promotion = null;
        if(!params[2].isEmpty()){
            if(currentGame.game().getBoard().getPiece(startPos).getPieceType() == ChessPiece.PieceType.PAWN){
                if(currentColor == ChessGame.TeamColor.WHITE && endPos.getRow() == 8 || currentColor == ChessGame.TeamColor.BLACK && endPos.getRow() ==1){
                    promotion = getPromotion(params[2]);
                }
                else{
                    throw new ResponseException(400, "Error: your pawn is not eligible for promotion");
                }
            }
            else {
                throw new ResponseException(400, "Error: promotion only applies to pawns");
            }
        }

        ChessMove move = new ChessMove(startPos, endPos, promotion);
        try{
            ChessClient.getClient().getWebSocketFacade().makeMove(move);
        } catch (IOException e){
            throw new ResponseException(400, "Error: " + e.getMessage());
        }

        return "Move executed";
    }

    private ChessPosition parsePosition(String ...position){
        int row = Character.getNumericValue(position[0].charAt(1));
        int col = Character.getNumericValue(position[0].charAt(0));
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType getPromotion(String ...params) throws ResponseException {
        ChessPiece.PieceType promotion =
        switch (params[0]){
            case "k" -> ChessPiece.PieceType.KNIGHT;
            case "b" -> ChessPiece.PieceType.BISHOP;
            case "q" -> ChessPiece.PieceType.QUEEN;
            case "r" -> ChessPiece.PieceType.ROOK;
            default -> throw new ResponseException(400, "Error: invalid promotion piece");
        };
        return promotion;
    }










}
