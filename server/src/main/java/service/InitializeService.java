package service;

import dataAccess.AuthDAO;
import dataAccess.exception.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class InitializeService extends Service {
    public InitializeService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public void clear() throws DataAccessException {
        userDAO.clearData();
        gameDAO.clearData();
        authDAO.clearData();
    }

}
