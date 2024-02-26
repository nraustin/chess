package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

import model.UserData;
import service.RegisterService;

public class RegisterHandler extends BaseHandler<UserData> {

    public RegisterHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public Object performService(UserData req) {
        RegisterService service = new RegisterService(userDAO, gameDAO, authDAO);
        try {
            return service.register(req);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Class<UserData> requestClass(){
        return UserData.class;
    }
}
