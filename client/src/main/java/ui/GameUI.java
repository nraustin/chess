package ui;

import chess.ChessGame;

public class GameUI implements UserInterface {

    public String eval(String cmd, String[] params){
        ChessGame game = new ChessGame();
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        return ChessBoardPrinter.printBoards(game, color);
    }


}
