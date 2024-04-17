package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {

    private int gameID;

    public LeaveCommand(UserGameCommand.CommandType commandType, String authToken, int gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
