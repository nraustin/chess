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
            if(authDAO.getAuth(authToken) == null) {
                throw new DataAccessException(401, "Error: unauthorized");
            }

            return gameDAO.listGames();

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }

    public Integer createGame(String gameName, String authToken) throws DataAccessException {
        try{
            if(authDAO.getAuth(authToken) == null){
                throw new DataAccessException(401, "Error: unauthorized");
            }
            if(gameName == null){
                throw new DataAccessException(400, "Error: bad request");
            }
            return gameDAO.createGame(gameName);

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }

    public void joinGame(int gameID, String playerColor, String authToken) throws DataAccessException{
        try{
            AuthData authenticatedUser = authDAO.getAuth(authToken);
            GameData targetGame = gameDAO.getGame(gameID);

            if(targetGame == null){
                throw new DataAccessException(400, "Error: bad request");
            }

            if(authenticatedUser != null){
                String username = authenticatedUser.username();

                if(playerColor != null) {
                    boolean whiteTeamAvailable = playerColor.equals("WHITE") && targetGame.whiteUsername() == null;
                    boolean blackTeamAvailable = playerColor.equals("BLACK") && targetGame.blackUsername() == null;
                    if (whiteTeamAvailable) {
                        GameData joinedGame = new GameData(gameID, username, targetGame.blackUsername(), targetGame.gameName(), targetGame.game());
                        gameDAO.updateGame(joinedGame);
                    } else if (blackTeamAvailable) {
                        GameData joinedGame = new GameData(gameID, targetGame.whiteUsername(),username, targetGame.gameName(), targetGame.game());
                        gameDAO.updateGame(joinedGame);
                    } else {
                        throw new DataAccessException(403, "Error: already taken");
                    }
                }
            } else {
                throw new DataAccessException(401, "Error: unauthorized");
            }

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }
}
