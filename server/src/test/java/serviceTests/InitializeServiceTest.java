package serviceTests;

import dataAccess.DataAccessException;
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

    void populateGamesDirectly(){
        testGame = gameDAO.createGame("Chess is fun");

        Assertions.assertEquals(testGame, gameDAO.getGame(testGame).gameID());

        populatedGames.add(gameDAO.getGame(testGame));

    }

    void populateAuthDirectly(){
        testAuthData = authDAO.createAuth("here for a good time");

        Assertions.assertEquals(testAuthData, authDAO.getAuth(testAuthData.authToken()));

        populatedAuth.add(testAuthData);
    }

    void populateUsersDirectly(){
        testUser = new UserData("u1", "p1", "e1");

        userDAO.createUser(testUser);

        Assertions.assertEquals(testUser, userDAO.getUser(testUser.username()));

        populatedUsers.add(testUser);
    }
}