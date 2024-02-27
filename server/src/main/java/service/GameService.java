package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;

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

    public void joinGame(int gameID, String playerColor, String authToken) throws DataAccessException{
        try{
            String username = authDAO.getAuth(authToken).username();
            GameData targetGame = gameDAO.getGame(gameID);

            if(playerColor != null) {
                boolean whiteTeamAvailable = playerColor == "WHITE" && targetGame.whiteUsername() == null;
                boolean blackTeamAvailable = playerColor == "BLACK" && targetGame.blackUsername() == null;
                if (whiteTeamAvailable) {
                    gameDAO.updateGame(username, targetGame, playerColor, targetGame.blackUsername());
                } else if (blackTeamAvailable) {
                    gameDAO.updateGame(targetGame.whiteUsername(), targetGame, playerColor, username);
                } else {
                    throw new DataAccessException(403, "Error: already taken");
                }
            }

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }
}
