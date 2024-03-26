package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.List;

public class ChessBoardPrinter {

    public static String printBoards(ChessGame game, ChessGame.TeamColor color){
        ChessBoard board = game.getBoard();
        StringBuilder s = new StringBuilder();

        int rowStart = (color == ChessGame.TeamColor.WHITE ? 8 : 1);
        int rowEnd = (color == ChessGame.TeamColor.WHITE ? 0 : 9);
        int i = (color == ChessGame.TeamColor.WHITE ? -1 : 1);

        s.append(EscapeSequences.CLEAR_FORMAT);
        columnLabelPrinter(s, color);
        s.append("   \n");

        for(int row = 8; row > 0; row--) {
            s.append(" ").append(row).append(" ");
            for(int col = 1; col < 9; col++){
                s.append((col + row) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_DARK_BROWN : EscapeSequences.SET_BG_COLOR_LIGHT_YELLOW);
                ChessPosition position = new ChessPosition(row, col);
                if(board.getPiece(position) != null){
                    s.append(EscapeSequences.EMPTY);
                    // should refactor
                    switch (board.getPiece(position).getPieceType()){
                        case QUEEN -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN);
                        case KING -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING);
                        case KNIGHT -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT);
                        case BISHOP -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP);
                        case ROOK -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK);
                        case PAWN -> s.append(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN);
                    }
                    s.append(EscapeSequences.EMPTY);
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
        s.append("   \u2002\u2009");
        if(color == ChessGame.TeamColor.WHITE){
            for(String label : columnLabels){
                s.append(label).append(" \u2003");
            }
        }
        else {
            for(int i = 7; i >=0; i--){
                s.append(columnLabels[i]).append(" \u2003");
            }
        }
        return s;
    }

}
