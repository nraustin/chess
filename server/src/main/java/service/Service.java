package service;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;

import java.util.UUID;

public abstract class Service {
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected AuthDAO authDAO;

    public Service(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }


}
