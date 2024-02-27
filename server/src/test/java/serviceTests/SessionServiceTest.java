package serviceTests;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.RegisterService;
import service.SessionService;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionServiceTest extends ServiceTest {

    private UserData testUser;
    private AuthData testUserAuthData;

    @BeforeEach
    void start() throws DataAccessException{
        initializeDAOs();
        testUser = registerTestUser();
    }

    @DisplayName("Register a test user for session tests")
    UserData registerTestUser() throws DataAccessException{
        UserData user = new UserData("nick", "issuper", "coolandhotmail.com");
        testUserAuthData = new RegisterService(userDAO, gameDAO, authDAO).register(user);

        Assertions.assertEquals(user, userDAO.getUser(user.username()));
        Assertions.assertEquals(testUserAuthData, authDAO.getAuth(testUserAuthData.authToken()));

        return user;
    }

    @Test
    @DisplayName("Properly login a user")
    void loginPositive() throws DataAccessException{
        AuthData newAuthData = new SessionService(userDAO, gameDAO, authDAO).login(testUser);

        Assertions.assertEquals(newAuthData, authDAO.getAuth(newAuthData.authToken()));
    }

    @Test
    @DisplayName("Login with invalid credentials")
    void loginNegative() {
        DataAccessException e = new DataAccessException(401, "Error: unauthorized");
        UserData imposter = new UserData("nick", "isnotsuper", "coolandhotmail.com");

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> new SessionService(userDAO, gameDAO, authDAO).login(imposter), "Should not login with invalid credentials");

        Assertions.assertEquals(e.getStatusCode(), thrown.getStatusCode());
        Assertions.assertEquals(e.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Properly logout a user")
    void logoutPositive() throws DataAccessException{
        new SessionService(userDAO, gameDAO, authDAO).logout(authDAO.getAuth(testUserAuthData.authToken()).authToken());

        AuthData authData = authDAO.getAuth(testUser.username());

        Assertions.assertEquals(authData, null);
    }

    @Test
    @DisplayName("Logout with invalid authToken")
    void logoutNegative() {
        DataAccessException e = new DataAccessException(401, "Error: unauthorized");
        String invalidAuthToken = "thisTokenDoesntExistInAuthDB";

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> new SessionService(userDAO, gameDAO, authDAO).logout(invalidAuthToken), "Should not logout with invalid authToken");

        Assertions.assertEquals(e.getStatusCode(), thrown.getStatusCode());
        Assertions.assertEquals(e.getMessage(), thrown.getMessage());
    }
}
