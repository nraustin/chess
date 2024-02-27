package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashSet;

public class GameService extends Service{

    public GameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public HashSet<GameData> listGames(String authToken) throws DataAccessException {
        try{
            authDAO.getAuth(authToken);
            return gameDAO.listGames();

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }

    public Integer createGame(String gameName, String authToken) throws DataAccessException {
        try{
            authDAO.getAuth(authToken);
            return gameDAO.createGame(gameName);

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }
}
