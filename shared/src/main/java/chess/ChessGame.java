package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        if(piece == null){
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new HashSet<>();


        for (ChessMove move: moves){
            // Save as reference to preserve game state
            ChessBoard unmodifiedBoard = board;
            // Simulate move
            board = simulateBoard(move, piece);
            if(!isInCheck(piece.getTeamColor())){
                legalMoves.add(move);
            }
            // Restore board game state
            board = unmodifiedBoard;
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
        ChessPiece piece = board.getPiece(move.getStartPosition());
        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());

        if(!legalMoves.contains(move)){
            throw new InvalidMoveException("Invalid move");

        }
        else if(piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("Other team's turn");
        }
        else{
            board = simulateBoard(move, piece);
        }

        teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;

    }

    public ChessBoard simulateBoard(ChessMove move, ChessPiece piece){
        // Create simulated board
        simulationBoard = new ChessBoard(board);
        boolean promotion = piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null;

        // Make moves on new board
        simulationBoard.addPiece(move.getStartPosition(), null);
        simulationBoard.addPiece(move.getEndPosition(), promotion ? new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()) : piece);

        return simulationBoard;
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
                if(board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != teamColor){
                    ChessPiece opponent = board.getPiece(testPosition);
                    Collection<ChessMove> opponentMoves = opponent.pieceMoves(board, testPosition);
                    for(ChessMove opponentMove: opponentMoves){
                        if(board.getPiece(opponentMove.getEndPosition()) != null &&
                                board.getPiece(opponentMove.getEndPosition()).getTeamColor() == teamColor &&
                                board.getPiece(opponentMove.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
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
        for(int row = 1; row < 9; row++){
            for(int col = 1; col < 9; col++){
                ChessPosition testPosition = new ChessPosition(row, col);
                if(board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() == teamColor){
                    if(!validMoves(testPosition).isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */

    /**
     * DRY?
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for(int row = 1; row < 9; row++){
            for(int cols = 1; cols < 9; cols++){
                ChessPosition testPosition = new ChessPosition(row, cols);
                if(board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() == teamColor){
                    if(!validMoves(testPosition).isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board) && Objects.equals(simulationBoard, chessGame.simulationBoard) && Objects.equals(piece, chessGame.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board, simulationBoard, piece);
    }
}
