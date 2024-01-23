package chess.pieces;

import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
public class KingMovesCalculator implements PieceMovesCalculator{

    int[][] kingDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        return PieceMovesCalculator.lessMoves(board, startPosition, kingDirections);
    }
}
