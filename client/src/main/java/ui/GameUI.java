package ui;

import chess.ChessGame;

public class GameUI implements UserInterface {

    public String eval(String cmd, String[] params){
        ChessGame game = new ChessGame();
        return ChessBoardPrinter.printBoard(game, ChessGame.TeamColor.WHITE) + ChessBoardPrinter.printBoard(game, ChessGame.TeamColor.BLACK);
    }


}
