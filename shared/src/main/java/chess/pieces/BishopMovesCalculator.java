package chess.pieces;

import chess.ChessPiece;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    private int[][] bishopDirections = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        return PieceMovesCalculator.slidingMoves(board, startPosition, bishopDirections);
    }
}

