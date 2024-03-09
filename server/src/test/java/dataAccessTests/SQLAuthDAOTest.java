package dataAccessTests;

import dataAccess.exception.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLAuthDAOTest extends SQLDAOTest{

    private UserData testUser;

    public SQLAuthDAOTest() throws DataAccessException {
    }

    @BeforeEach
    void start() throws DataAccessException {
        initializeDAOs();
        testUser = new UserData("nick", "issuper", "coolandhotmail.com");
    }

    @Test
    @DisplayName("Properly create an authToken")
    void createAuthPositive() throws DataAccessException {
        AuthData testAuthData = authDAO.createAuth(testUser.username());

        Assertions.assertNotNull(testAuthData.authToken());
    }

    // Kind of an odd test with how I have things set up
    @Test
    @DisplayName("Create two authTokens for same user")
    void createAuthNegative() throws DataAccessException {
        AuthData testAuthData = authDAO.createAuth(testUser.username());
        AuthData wrongAuthData = authDAO.createAuth(testUser.username());

        Assertions.assertNotEquals(wrongAuthData.authToken(), testAuthData.authToken());

    }

    @Test
    @DisplayName("Properly retrieve authToken")
    void getAuthPositive() throws DataAccessException {
        AuthData testAuthData = authDAO.createAuth(testUser.username());

        Assertions.assertEquals(testAuthData.authToken(), authDAO.getAuth(testAuthData.authToken()).authToken());
    }

    @Test
    @DisplayName("Attempt to retrieve non existing authToken")
    void getAuthNegative() throws DataAccessException {
        String testToken = UUID.randomUUID().toString();

        Assertions.assertNull(authDAO.getAuth(testToken));
    }

    @Test
    @DisplayName("Properly delete authToken")
    void deleteAuthPositive() throws DataAccessException {
        AuthData testAuthData = authDAO.createAuth(testUser.username());

        authDAO.deleteAuth(testAuthData);

        Assertions.assertNull(authDAO.getAuth(testAuthData.authToken()));

    }

    @Test
    @DisplayName("Delete wrong authToken")
    void deleteAuthNegative() throws DataAccessException {
        AuthData toBeDeleted = authDAO.createAuth(testUser.username());
        AuthData toNotBeDeleted = authDAO.createAuth("they call me Jane");

        authDAO.deleteAuth(toBeDeleted);

        Assertions.assertNotNull(authDAO.getAuth(toNotBeDeleted.authToken()));
    }

    @Test
    @DisplayName("Clear auth table")
    void clearAuthPositive() throws DataAccessException {
        AuthData testAuthData = authDAO.createAuth(testUser.username());
        authDAO.clearData();

        Assertions.assertNull(authDAO.getAuth(testAuthData.authToken()));
    }
}
