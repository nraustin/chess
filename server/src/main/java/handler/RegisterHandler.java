package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

import model.UserData;
import service.RegisterService;
import spark.Request;

public class RegisterHandler extends BaseHandler {

    public RegisterHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public Object performService(Object reqObject, Request res) throws DataAccessException {
        RegisterService service = new RegisterService(userDAO, gameDAO, authDAO);
        return service.register((UserData)reqObject);
    }

    public Class requestClass(Request req){
        return UserData.class;
    }
}
