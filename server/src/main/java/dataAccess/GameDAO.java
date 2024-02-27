package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    public Integer createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public HashSet<GameData> listGames();
    public void updateGame(GameData game);
    public void clearData();
}
