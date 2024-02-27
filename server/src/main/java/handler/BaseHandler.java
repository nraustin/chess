package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.*;

public abstract class BaseHandler<T> implements Handler<T> {

    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected AuthDAO authDAO;

    private Gson serializer = new Gson();

    public BaseHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public T deserializeRequest(Request req) {
        return serializer.fromJson(req.body(), this.requestClass(req));
    }

    public Object serializeResponse(Object res) {
//        if(res == "{}"){
//            return res;
//        }
        return serializer.toJson(res);
    }

    public Object handle(Request req, Response res) throws DataAccessException {
        // Testing for joinGame
        // Last return of the generics?
        T reqObject = deserializeRequest(req);
        System.out.println(String.format("Deserialized object: %s", reqObject));

        // Retrieve service response object
        Object resObject = this.performService(reqObject, req);

        System.out.println(String.format("What's in base handler before return: %s", resObject));

        res.status(200);
        return serializeResponse(resObject);
    }

}
