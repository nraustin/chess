package response;

import model.GameData;

import java.util.HashSet;

public class GameResponse extends Response{

    private HashSet<GameData> games;
    private int gameID;

    public GameResponse(String message, HashSet<GameData> games){
        super(message);
        this.games = games;
    }

    public GameResponse(Integer gameID){
        super(null);
        this.gameID = gameID;
    }
}
