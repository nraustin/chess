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
        return serializer.fromJson(req.body(), this.requestClass());
    }

    public Object serializeResponse(Object res) {
        return serializer.toJson(res);
    }

    public Object handle(Request req, Response res) throws DataAccessException {
        // Processed request must be generic in order for this to work
        T reqObject = deserializeRequest(req);
        String authToken = req.headers("Authorization");
        // Retrieve service response object
        Object resObject = this.performService(reqObject);

        res.status(200);
        return serializeResponse(resObject);
    }



}
