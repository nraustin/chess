package dataAccessTests;

import dataAccess.*;
import dataAccess.exception.DataAccessException;
import dataAccess.sql.SQLAuthDAO;
import dataAccess.sql.SQLGameDAO;
import dataAccess.sql.SQLUserDAO;

public abstract class SQLDAOTest {

    protected final UserDAO userDAO;
    protected final GameDAO gameDAO;
    protected final AuthDAO authDAO;

    public SQLDAOTest() throws DataAccessException {
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
