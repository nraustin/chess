package service;

import dataAccess.AuthDAO;
import dataAccess.exception.DataAccessException;
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

            if(targetUser == null || !userDAO.verifyUser(user.username(), user.password())){
                System.out.println(String.format("tguser: %s", targetUser));
                throw new DataAccessException(401, "Error: unauthorized");
            }
            System.out.println(String.format("user from login: %s", user.username()));
            AuthData authentication = authDAO.createAuth(user.username());
            System.out.println(String.format("authData from login: %s", authentication));
            return authentication;
        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }

    public void logout(String authToken) throws DataAccessException{
        try{
            AuthData targetAuthData = authDAO.getAuth(authToken);
            if(targetAuthData == null){
                throw new DataAccessException(401, "Error: unauthorized");
            }
            System.out.println(String.format("authData from login: %s", targetAuthData));
            authDAO.deleteAuth(targetAuthData);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }



}
