package chess.pieces;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
public class PawnMovesCalculator implements PieceMovesCalculator {
    private int[][] whitePawnDirections = {{1, 0}, {1, 1}, {1, -1}};
    private int[][] blackPawnDirections = {{-1, 0}, {-1, -1}, {-1, 1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        if(board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            return PieceMovesCalculator.lessMoves(board, startPosition, whitePawnDirections);
        }
        else{
            return PieceMovesCalculator.lessMoves(board, startPosition, blackPawnDirections);
        }
    }
}
