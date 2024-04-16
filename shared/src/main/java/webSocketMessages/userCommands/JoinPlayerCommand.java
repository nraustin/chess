package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{

    private int gameID;
    ChessGame.TeamColor color;

    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor color, boolean observer){
        super(authToken);
        this.commandType = observer ? CommandType.JOIN_OBSERVER : CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.color = color;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor(){
        return color;
    }
}
