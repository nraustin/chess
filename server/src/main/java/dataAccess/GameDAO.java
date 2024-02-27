package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    public Integer createGame(String gameName);
    public GameData getGame(int gameID);
    public HashSet<GameData> listGames();
    public void updateGame(GameData joinedGame);
    public void clearData();
}
