package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    private ChessBoard simulationBoard;
    private ChessPosition simulatedMove;
    private ChessPiece piece;
    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new HashSet<>();

        for (ChessMove move: moves){
            // Simulate move on new board
            simulationBoard = new ChessBoard(board);
            boolean validMove = simulateMove(simulationBoard, move, piece);
            if(validMove){
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    public boolean simulateMove(ChessBoard simulationBoard, ChessMove candidateMove, ChessPiece piece){

        simulationBoard.addPiece(candidateMove.getStartPosition(), null);
        simulatedMove = candidateMove.getEndPosition();
        simulationBoard.addPiece(simulatedMove, piece);

        return !isInCheck(piece.getTeamColor());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for(int row = 1; row < 9; row++){
            for(int col = 1; col < 9; col++){
                ChessPosition testPosition = new ChessPosition(row, col);
                if(simulationBoard.getPiece(testPosition) != null && simulationBoard.getPiece(testPosition).getTeamColor() != teamColor){
                    ChessPiece opponent = simulationBoard.getPiece(testPosition);
                    Collection<ChessMove> opponentMoves = opponent.pieceMoves(simulationBoard, testPosition);
                    for(ChessMove opponentMove: opponentMoves){
                        if(simulationBoard.getPiece(opponentMove.getEndPosition()) != null &&
                           simulationBoard.getPiece(opponentMove.getEndPosition()).getTeamColor() == teamColor &&
                           simulationBoard.getPiece(opponentMove.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
