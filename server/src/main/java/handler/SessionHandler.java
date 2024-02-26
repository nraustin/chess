package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.SessionService;
import spark.Request;

public class SessionHandler extends BaseHandler{

    public SessionHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public Object performService(Object reqObject, Request req) throws DataAccessException {
        SessionService service = new SessionService(userDAO, gameDAO, authDAO);
        if(reqObject instanceof UserData){
            return service.login((UserData)reqObject);
        }
        service.logout(req.headers("Authorization"));
        return null;
    }

    public Class requestClass(){
        return UserData.class;
    }
}
