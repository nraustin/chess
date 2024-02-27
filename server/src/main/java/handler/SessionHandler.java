package handler;

import com.google.gson.Gson;
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
//        if(reqObject instanceof UserData){
//            return service.login((UserData)reqObject);
//        }
//        service.logout(req.headers("Authorization"));
//        return null;
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
        if(req.requestMethod() == "POST"){
            return UserData.class;
        }
        return Void.class;
    }
}
