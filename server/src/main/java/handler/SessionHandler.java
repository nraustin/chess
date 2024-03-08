package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.exception.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.SessionService;
import spark.Request;

import java.net.CookieHandler;

public class SessionHandler extends BaseHandler{

    public SessionHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public Object performService(Object reqObject, Request req) throws DataAccessException {
        SessionService service = new SessionService(userDAO, gameDAO, authDAO);

        switch(req.requestMethod()){
            case "POST":
                return service.login((UserData)reqObject);
            case "DELETE":
                service.logout(req.headers("Authorization"));
                return null;
            default:
                throw new DataAccessException(400, "Error: unsupported request method");
        }
    }

    public Class requestClass(Request req){
        if(req.requestMethod().equals("POST")){
            return UserData.class;
        }
        return void.class;
    }
}
