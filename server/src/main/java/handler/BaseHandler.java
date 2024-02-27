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
        System.out.println(String.format("Request class: %s", this.requestClass()));
        return serializer.fromJson(req.body(), this.requestClass());
    }

    public Object serializeResponse(Object res) {
        if(res == "{}"){
            System.out.println("empty res");
            return res;
        }
        Object ser = serializer.toJson(res);
        System.out.println(String.format("Serialized object: %s", ser));
        return ser;
    }

    public Object handle(Request req, Response res) throws DataAccessException {
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
