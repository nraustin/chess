package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{

    private int gameID;
    // Pretty sure tests should pass irrespective of variable naming
    ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(UserGameCommand.CommandType commandType, String authToken, int gameID, ChessGame.TeamColor playerColor){
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor(){
        return playerColor;
    }
}
