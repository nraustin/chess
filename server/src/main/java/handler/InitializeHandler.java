package handler;

import dataAccess.AuthDAO;
import dataAccess.exception.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

import service.InitializeService;
import spark.Request;
import spark.Response;

public class InitializeHandler extends BaseHandler{
    public InitializeHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }
    @Override
    public Object handle(Request req, Response res) throws DataAccessException {
        Object resObject = this.performService(null, null);

        res.status(200);
        return serializeResponse(resObject);
    }
    public Object performService(Object reqObject, Request req) {
        InitializeService service = new InitializeService(userDAO, gameDAO, authDAO);
        try {
            service.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public Class requestClass(Request req){
        return void.class;
    }
}
