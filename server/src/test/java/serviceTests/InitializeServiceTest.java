package serviceTests;

import dataAccess.exception.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.InitializeService;

import java.util.HashSet;

public class InitializeServiceTest extends ServiceTest{

    private HashSet<GameData> populatedGames = new HashSet<>();
    private HashSet<AuthData> populatedAuth = new HashSet<>();
    private HashSet<UserData> populatedUsers = new HashSet<>();

    private int testGame;
    private AuthData testAuthData;
    private UserData testUser;

    public InitializeServiceTest() throws DataAccessException {
    }

    @Test
    @DisplayName("Clear all data from DAOs")
    void clearData() throws DataAccessException {

        populateUsersDirectly();
        populateGamesDirectly();
        populateAuthDirectly();

        new InitializeService(userDAO, gameDAO, authDAO).clear();

        Assertions.assertNull(userDAO.getUser(testUser.username()));
        Assertions.assertNull(gameDAO.getGame(testGame));
        Assertions.assertNull(authDAO.getAuth(testAuthData.authToken()));
    }

    void populateGamesDirectly() throws DataAccessException {
        testGame = gameDAO.createGame("Chess is fun").gameID();

        Assertions.assertEquals(testGame, gameDAO.getGame(testGame).gameID());

        populatedGames.add(gameDAO.getGame(testGame));

    }

    void populateAuthDirectly() throws DataAccessException {
        testAuthData = authDAO.createAuth("here for a good time");

        Assertions.assertEquals(testAuthData, authDAO.getAuth(testAuthData.authToken()));

        populatedAuth.add(testAuthData);
    }

    void populateUsersDirectly() throws DataAccessException {
        testUser = new UserData("u1", "p1", "e1");

        userDAO.createUser(testUser);

        Assertions.assertEquals(testUser.username(), userDAO.getUser(testUser.username()).username());

        populatedUsers.add(testUser);
    }
}
