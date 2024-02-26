package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class LoginService extends Service {
    public LoginService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public AuthData login(UserData user) throws DataAccessException{
        try{
            UserData targetUser = userDAO.getUser(user.username());

            if(targetUser == null || !targetUser.password().equals(user.password())){
                System.out.println(targetUser);
                throw new DataAccessException(401, "Error: unauthorized");
            }
            AuthData authToken = authDAO.createAuth(user.username());
            return authToken;
        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }



}
