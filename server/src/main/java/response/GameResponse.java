package response;

import model.GameData;

import java.util.HashSet;

public class GameResponse extends Response{

    private HashSet<GameData> games;
    private int gameID;

    public GameResponse(HashSet<GameData> games){
        super(null);
        this.games = games;
    }

    public GameResponse(int gameID){
        super(null);
        this.gameID = gameID;
    }
}
