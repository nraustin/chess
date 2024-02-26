package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

import service.InitializeService;
import spark.Request;
import spark.Response;

public class InitializeHandler extends BaseHandler<Void>{
    public InitializeHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }
    @Override
    public Object handle(Request req, Response res){
        String authToken = req.headers("Authorization");
        Object resObject = this.performService(null);

        res.status(200);
        return serializeResponse(resObject);
    }
    @Override
    public Object performService(Void req) {
        InitializeService service = new InitializeService(userDAO, gameDAO, authDAO);
        try {
            service.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Class<Void> requestClass(){
        return null;
    }
}
