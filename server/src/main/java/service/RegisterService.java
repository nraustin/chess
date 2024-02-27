package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class RegisterService extends Service{
    public RegisterService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public AuthData register(UserData user) throws DataAccessException {
        try{
            userDAO.createUser(user);
            AuthData authentication = authDAO.createAuth(user.username());

            return authentication;

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }
}
