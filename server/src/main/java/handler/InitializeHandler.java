package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;
import service.InitializeService;

public class InitializeHandler extends BaseHandler<Void>{
    public InitializeHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
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

    public Class<Void> requestClass(){
        return null;
    }
}
