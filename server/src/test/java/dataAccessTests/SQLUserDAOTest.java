package dataAccessTests;

import dataAccess.exception.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLUserDAOTest extends SQLDAOTest{

    private UserData testUser;

    public SQLUserDAOTest() throws DataAccessException {
    }

    @BeforeEach
    void start() throws DataAccessException {
        initializeDAOs();
        testUser = new UserData("nick", "issuper", "coolandhotmail.com");
    }

    @Test
    @DisplayName("Properly create a user")
    void createUserPositive() throws DataAccessException {
        userDAO.createUser(testUser);

        Assertions.assertEquals(testUser.username(), userDAO.getUser(testUser.username()).username());
    }

    @Test
    @DisplayName("Create a user with existing username")
    void createUserNegative() throws DataAccessException {
        DataAccessException e = new DataAccessException(500, "unable to update database: INSERT INTO user (username, password, email) VALUES (?, ?, ?), Duplicate entry 'nick' for key 'user.PRIMARY'");
        UserData copycat = new UserData(testUser.username(), "they call me", "Stacy");

        createUserPositive();
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> userDAO.createUser(copycat));

        Assertions.assertEquals(e.getStatusCode(), thrown.getStatusCode());
        Assertions.assertEquals(e.getMessage(), thrown.getMessage());

    }

    @Test
    @DisplayName("Properly retrieve a user")
    void findUserPositive() throws DataAccessException{
        createUserPositive();

        Assertions.assertEquals(testUser.username(), userDAO.getUser(testUser.username()).username());
    }

    @Test
    @DisplayName("Retrieve non existing user")
    void findUserNegative() throws DataAccessException{
        Assertions.assertNull(userDAO.getUser(testUser.username()));
    }

    @Test
    @DisplayName("Verify correct credentials")
    void verifyUserPositive() throws DataAccessException {
        createUserPositive();

        Assertions.assertTrue(userDAO.verifyUser(userDAO.getUser(testUser.username()).username(), testUser.password()));
    }

    @Test
    @DisplayName("Attempt to verify incorrect credentials")
    void verifyUserNegative() throws DataAccessException {
        DataAccessException e = new DataAccessException(401, "Error: unauthorized");
        createUserPositive();
        UserData imposter = new UserData(testUser.username(), "they call me", "her");

        Assertions.assertFalse(userDAO.verifyUser(userDAO.getUser(testUser.username()).username(), imposter.password()));
    }

    @Test
    @DisplayName("Clear user table")
    void clearUserPostive() throws DataAccessException {
        createUserPositive();
        userDAO.clearData();

        Assertions.assertNull(userDAO.getUser(testUser.username()));
    }

}
