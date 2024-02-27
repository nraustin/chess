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
    @Test
    @DisplayName("Clear all data from DAOs")
    void clearData() throws DataAccessException {

        populateUsersDirectly();
        populateGamesDirectly();
        populateAuthDirectly();

        new InitializeService(userDAO, gameDAO, authDAO).clear();

        Assertions.assertTrue(userDAO.listUsers().isEmpty());
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
        Assertions.assertTrue(authDAO.listAuth().isEmpty());
    }

    void populateGamesDirectly(){
        int g1 = gameDAO.createGame("Chess is fun");
        int g2 = gameDAO.createGame("except");
        int g3 = gameDAO.createGame("for when it's testing time");

        Assertions.assertEquals(g1, gameDAO.getGame(g1).gameID());
        Assertions.assertEquals(g2, gameDAO.getGame(g2).gameID());
        Assertions.assertEquals(g3, gameDAO.getGame(g3).gameID());

        populatedGames.add(gameDAO.getGame(g1));
        populatedGames.add(gameDAO.getGame(g2));
        populatedGames.add(gameDAO.getGame(g3));
    }

    void populateAuthDirectly(){
        AuthData a1 = authDAO.createAuth("Its");
        AuthData a2 = authDAO.createAuth("getting");
        AuthData a3 = authDAO.createAuth("pretty late");

        Assertions.assertEquals(a1, authDAO.getAuth(a1.authToken()));
        Assertions.assertEquals(a2, authDAO.getAuth(a2.authToken()));
        Assertions.assertEquals(a3, authDAO.getAuth(a3.authToken()));

        populatedAuth.add(a1);
        populatedAuth.add(a2);
        populatedAuth.add(a3);
    }

    void populateUsersDirectly(){
        UserData u1 = new UserData("u1", "p1", "e1");
        UserData u2 = new UserData("u2", "p2", "e2");
        UserData u3 = new UserData("u3", "p3", "e3");

        userDAO.createUser(u1);
        userDAO.createUser(u2);
        userDAO.createUser(u3);

        Assertions.assertEquals(u1, userDAO.getUser(u1.username()));
        Assertions.assertEquals(u2, userDAO.getUser(u2.username()));
        Assertions.assertEquals(u3, userDAO.getUser(u3.username()));

        populatedUsers.add(u1);
        populatedUsers.add(u2);
        populatedUsers.add(u3);
    }
}
