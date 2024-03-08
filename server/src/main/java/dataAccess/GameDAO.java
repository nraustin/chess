package dataAccess;

import dataAccess.exception.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    public Integer createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public HashSet<GameData> listGames() throws DataAccessException;
    public void updateGame(GameData joinedGame) throws DataAccessException;
    public void clearData() throws DataAccessException;

}
