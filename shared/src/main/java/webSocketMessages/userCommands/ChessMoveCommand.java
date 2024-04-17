package webSocketMessages.userCommands;

import chess.ChessMove;

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

    public int getGameID(){
        return gameID;
    }
}
