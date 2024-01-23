package chess.pieces;

import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
public class RookMovesCalculator implements PieceMovesCalculator {
    private int[][] rookDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        return PieceMovesCalculator.slidingMoves(board, startPosition, rookDirections);
    }
}
