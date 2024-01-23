package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    // Thanks Java 8

    // Move calculation logic for Bishops, Rooks, Queens
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

                if (newPosition.validPosition()) {
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
                else{
                    break;
                }
            }
        }
        return legalMoves;
    }

    // Move calculation logic for Kings, Pawns, Knights
    public static Collection<ChessMove> lessMoves(ChessBoard board, ChessPosition startPosition, int[][] moves) {

        Collection<ChessMove> legalMoves = new HashSet<>();

        for (int i = 0; i < moves.length; i++) {
            int row = startPosition.getRow();
            int col = startPosition.getColumn();

            ChessPosition newPosition = new ChessPosition(row + moves[i][0], col + moves[i][1]);

            if (newPosition.validPosition()) {
                // Pawn case
                if (board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
                    // Aha
                    int direction = board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
                    // Forward advancement
                    if(moves[i][1] == 0){
                        if(board.getPiece(newPosition) == null){
                            ChessMove singleForward = new ChessMove(startPosition, newPosition, null);
                            legalMoves.add(singleForward);
                            // Check for open space for double advancement
                            ChessPosition twoSquaresAhead = new ChessPosition(newPosition.getRow()+direction, newPosition.getColumn());
                            if(board.getPiece(twoSquaresAhead) == null){
                                ChessMove doubleForward = new ChessMove(startPosition, twoSquaresAhead, null);
                                legalMoves.add(doubleForward);
                            }
                        }
                    }
                    // Diagonal enemy capture
                    else {
                        if(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                            ChessMove captureDiagonal = new ChessMove(startPosition, newPosition, null);
                            legalMoves.add(captureDiagonal);
                        }
                    }

                }
            }
        }
        return legalMoves;
    }

}
