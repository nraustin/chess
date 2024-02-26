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

    @Override
    public Object performService(UserData req) throws DataAccessException {
        RegisterService service = new RegisterService(userDAO, gameDAO, authDAO);
        return service.register(req);
    }

    public Class<UserData> requestClass(){
        return UserData.class;
    }
}
