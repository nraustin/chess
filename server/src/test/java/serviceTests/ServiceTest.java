package serviceTests;

import dataAccess.*;
import service.Service;

public abstract class ServiceTest {

    protected final UserDAO userDAO;
    protected final GameDAO gameDAO;
    protected final AuthDAO authDAO;

    public ServiceTest(){
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
    }

    protected void initializeDAOs(){
        userDAO.clearData();
        gameDAO.clearData();
        authDAO.clearData();
    }
}
