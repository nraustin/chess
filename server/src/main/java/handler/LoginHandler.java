package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;
import service.LoginService;

public class LoginHandler extends BaseHandler<UserData>{
    public LoginHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }


    public Object performService(UserData req) throws DataAccessException {
        LoginService service = new LoginService(userDAO, gameDAO, authDAO);
        return service.login(req);
    }

    public Class<UserData> requestClass(){
        return UserData.class;
    }
}
