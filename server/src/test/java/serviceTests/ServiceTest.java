package serviceTests;

import dataAccess.*;
import dataAccess.exception.DataAccessException;
import dataAccess.sql.SQLAuthDAO;
import dataAccess.sql.SQLGameDAO;
import dataAccess.sql.SQLUserDAO;
import service.Service;

public abstract class ServiceTest {

    protected final UserDAO userDAO;
    protected final GameDAO gameDAO;
    protected final AuthDAO authDAO;

    public ServiceTest() throws DataAccessException {
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
    }

    protected void initializeDAOs() throws DataAccessException {
        userDAO.clearData();
        gameDAO.clearData();
        authDAO.clearData();
    }
}
