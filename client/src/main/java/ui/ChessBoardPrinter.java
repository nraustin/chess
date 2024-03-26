package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.List;

public class ChessBoardPrinter {

    public static String printBoard(ChessGame game, ChessGame.TeamColor color){
        ChessBoard board = game.getBoard();
        board.resetBoard();
        StringBuilder s = new StringBuilder();

        s.append(EscapeSequences.CLEAR_FORMAT).append("\n");
        columnLabelPrinter(s, color);
        s.append("   \n");

        // meh
        int rowStart = (color == ChessGame.TeamColor.WHITE ? 8 : 1);
        int rowEnd = (color == ChessGame.TeamColor.WHITE ? 0 : 9);
        int rowIncrement = (color == ChessGame.TeamColor.WHITE ? -1 : 1);
        int colStart = (rowStart == 8 ? 1 : 8);
        int colIncrement = (rowIncrement == -1 ? 1 : -1);

        for(int row = rowStart; rowEnd == 0 ? row > rowEnd : row < rowEnd; row += rowIncrement) {
            s.append(" ").append(row).append(" ");
            for(int col = colStart; rowEnd == 0 ? col < 9: col > 0; col += colIncrement){
                s.append((col + row) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_DARK_GREY : EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                ChessPosition position = new ChessPosition(row, col);
                if(board.getPiece(position) != null){
                    // should refactor
                    switch (board.getPiece(position).getPieceType()){
                        case QUEEN -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN);
                        case KING -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING);
                        case KNIGHT -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT);
                        case BISHOP -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP);
                        case ROOK -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK);
                        case PAWN -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN);
                    }
                } else{
                    s.append(EscapeSequences.EMPTY);
                }
            }
            s.append(EscapeSequences.CLEAR_FORMAT);
            s.append(" ").append(row).append("\n");
        }
        columnLabelPrinter(s, color);

        s.append("\n").append(EscapeSequences.CLEAR_FORMAT).append(EscapeSequences.SET_TEXT_COLOR_GREEN);
        return s.toString();
    }

    private static StringBuilder columnLabelPrinter(StringBuilder s, ChessGame.TeamColor color){
        String[] columnLabels = {"a", "b", "c", "d", "e", "f", "g", "h"};
        s.append("   \u2002\u2009\u200A");
        if(color == ChessGame.TeamColor.WHITE){
            for(String label : columnLabels){
                s.append(label).append("\u2002\u2002\u2004\u2004"); // It just works
            }
        }
        else {
            for(int i = 7; i >=0; i--){
                s.append(columnLabels[i]).append("\u2002\u2002\u2004\u2004"); // I am somewhat proud of this
            }
        }
        return s;
    }

}
