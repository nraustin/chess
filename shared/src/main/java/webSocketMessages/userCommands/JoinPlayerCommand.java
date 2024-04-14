package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{

    private int gameID;
    ChessGame.TeamColor color;

    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor color){
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.color = color;
    }
}
