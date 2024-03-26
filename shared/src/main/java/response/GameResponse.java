package response;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public class GameResponse extends Response {

    private HashSet<GameData> games;

    private GameData game;
    private int gameID;

    public GameResponse(HashSet<GameData> games){
        super(null);
        this.games = games;
    }

    public GameResponse(GameData game){
        super(null);
        this.game = game;
        this.gameID = game.gameID();
    }

    public HashSet<GameData> getGames(){
        return games;
    }

    public GameData getGame(){
        return game;
    }

    public int getGameID(){
        return gameID;
    }
}
