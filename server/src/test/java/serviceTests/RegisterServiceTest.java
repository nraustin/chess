package serviceTests;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterServiceTest extends ServiceTest {

    @BeforeEach
    void start(){
        initializeDAOs();
    }

    @Test
    @DisplayName("Properly register a user")
    void registerPositive() throws DataAccessException{
        UserData user = new UserData("nick", "issuper", "coolandhotmail.com");
        AuthData authData = new RegisterService(userDAO, gameDAO, authDAO).register(user);

        Assertions.assertEquals(user, userDAO.getUser(user.username()));
        Assertions.assertEquals(authData, authDAO.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Register a user twice")
    void registerNegative() throws DataAccessException {
        DataAccessException e = new DataAccessException(403, "Error: already taken");
        UserData user = new UserData("nick", "issuper", "coolandhotmail.com");

        new RegisterService(userDAO, gameDAO, authDAO).register(user);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> new RegisterService(userDAO, gameDAO, authDAO).register(user), "Second register did not fail");

        Assertions.assertEquals(e.getStatusCode(), thrown.getStatusCode());
        Assertions.assertEquals(e.getMessage(), thrown.getMessage());
    }

}
