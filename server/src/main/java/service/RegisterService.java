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
//            if(user.username() == null || user.password() == null || user.email() == null){
//                throw new DataAccessException(400, "Error: bad request");
//            }
            userDAO.createUser(user);
            AuthData authToken = authDAO.createAuth(user.username());

            return authToken;

        } catch (DataAccessException e){
            throw new DataAccessException(e.getStatusCode(), e.getMessage());
        }
    }
}
