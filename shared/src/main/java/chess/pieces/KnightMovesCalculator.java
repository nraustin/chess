package chess.pieces;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
public class KnightMovesCalculator implements PieceMovesCalculator {

    int[][] knightDirections = {{1, 2}, {-1, -2}, {1, -2}, {-1, 2}, {2, 1}, {-2, -1}, {2, -1}, {-2, 1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        return PieceMovesCalculator.lessMoves(board, startPosition, knightDirections);
    }
}
