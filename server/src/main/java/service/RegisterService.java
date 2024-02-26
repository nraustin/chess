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
//        if(userDAO.getUser(user.username()) != null){
//            throw new DataAccessException("User already exists");
//        }
        userDAO.createUser(user);
        AuthData authToken = authDAO.createAuth(user.username());

        return authToken;
    }
}
