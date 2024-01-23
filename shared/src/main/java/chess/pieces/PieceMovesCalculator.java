package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    // Thanks Java 8

    // Move calculation logic for Bishops, Rooks, Queens
    public static Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition startPosition, int[][] moves) {

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

    // Move calculation logic for Pawns, Knights, Kings
    public static Collection<ChessMove> fixedMoves(ChessBoard board, ChessPosition startPosition, int[][] moves) {

        Collection<ChessMove> legalMoves = new HashSet<>();

        // TODO: refactor this unintuitive loop
        for (int i = 0; i < moves.length; i++) {
            int row = startPosition.getRow();
            int col = startPosition.getColumn();

            ChessPosition newPosition = new ChessPosition(row + moves[i][0], col + moves[i][1]);

            if (newPosition.validPosition()) {

                // Pawn case
                if (board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {

                    // Special pawn cases
                    boolean promotion = newPosition.getRow() == 8 && board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE ||
                            newPosition.getRow() == 1 && board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK;

                    boolean firstTurn = startPosition.getRow() == 2 && board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE ||
                            startPosition.getRow() == 7 && board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK;

                    // Checks for no column adjustment on a move for forward advancement
                    if(moves[i][1] == 0){
                        if(board.getPiece(newPosition) == null){
                            if(promotion){
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.ROOK));
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.BISHOP));
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.QUEEN));
                            }
                            else{
                                // Single forward advancement case
                                ChessMove singleForward = new ChessMove(startPosition, newPosition, null);
                                legalMoves.add(singleForward);

                                // Double forward advancement case
                                int direction = board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
                                ChessPosition twoSquaresAhead = new ChessPosition(newPosition.getRow()+direction, newPosition.getColumn());

                                if (firstTurn && board.getPiece(twoSquaresAhead) == null){
                                    ChessMove doubleForward = new ChessMove(startPosition, twoSquaresAhead, null);
                                    legalMoves.add(doubleForward);
                                }
                            }

                        }
                    }
                    // Diagonal enemy capture
                    else {
                        if(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                            if(promotion){
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.ROOK));
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.BISHOP));
                                legalMoves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.QUEEN));
                            }
                            else{
                                ChessMove captureDiagonal = new ChessMove(startPosition, newPosition, null);
                                legalMoves.add(captureDiagonal);
                            }
                        }
                    }

                }
                // Knight and King cases
                else if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                    legalMoves.add(new ChessMove(startPosition, newPosition, null));
                }
            }
        }
        return legalMoves;
    }

}
