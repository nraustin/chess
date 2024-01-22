package chess.pieces;

import chess.ChessPiece;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public static Collection<ChessMove> manyMoves(ChessBoard board, ChessPosition startPosition, int[][] moves) {

        Collection<ChessMove> legalMoves = new HashSet<>();


        for(int i = 0; i < moves.length; i++) {
            int row = startPosition.getRow();
            int col = startPosition.getColumn();

            while (true) {
                // Continue traversing valid positions in a direction
                row += moves[i][0];
                col += moves[i][1];

                ChessPosition newPosition = new ChessPosition(row, col);

                if (!newPosition.validPosition()) {
                    break;
                }
                // Open space
                if (board.getPiece(newPosition) == null) {
                    ChessMove legalMove = new ChessMove(startPosition, newPosition, null);
                    legalMoves.add(legalMove);
                }
                // Enemy capture
                else {
                    if (board.getPiece(startPosition).getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        ChessMove captureMove = new ChessMove(startPosition, newPosition, null);
                        legalMoves.add(captureMove);
                    }
                    break;
                }
            }
        }
        return legalMoves;
    }


}
