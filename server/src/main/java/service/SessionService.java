package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class SessionService extends Service {
    public SessionService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public AuthData login(UserData user) throws DataAccessException{
        try{
            UserData targetUser = userDAO.getUser(user.username());

            if(targetUser == null || !targetUser.password().equals(user.password())){
                throw new DataAccessException(401, "Error: unauthorized");
            }
            AuthData authentication = authDAO.createAuth(user.username());
            return authentication;
        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }

    public void logout(String authToken) throws DataAccessException{
        try{
            AuthData targetAuthData = authDAO.getAuth(authToken);
            authDAO.deleteAuth(targetAuthData);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }



}
