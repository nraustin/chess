package webSocketMessages.userCommands;

import chess.ChessMove;
import chess.ChessPosition;

public class ChessMoveCommand extends UserGameCommand{

    private ChessMove move;
    private int gameID;
    public ChessMoveCommand(String authToken, ChessMove move, int gameID) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
        this.gameID = gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public String parsedMove(){
        int col = move.getStartPosition().getColumn();
        int row = move.getStartPosition().getRow();
        int endCol = move.getEndPosition().getColumn();
        int endRow = move.getEndPosition().getRow();

        char parsedCol = (char) ('a' + col - 1);
        char parsedEndCol = (char) ('a' + endCol - 1);

        String startPos = "" + parsedCol + row;
        String endPos = "" + parsedEndCol + endRow;

        return startPos + " to " + endPos;
    }

    public int getGameID(){
        return gameID;
    }
}
