package request;

public class JoinGameRequest extends Request{

    private String playerColor;
    private int gameID;
    public JoinGameRequest(String playerColor, int gameID){
        super(null);
        this.playerColor = playerColor;
        this.gameID = gameID;
    };

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

}
