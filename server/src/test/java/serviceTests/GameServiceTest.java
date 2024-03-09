package serviceTests;

import dataAccess.exception.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest extends ServiceTest{

    private String authToken;
    private HashSet<GameData> populatedGames = new HashSet<>();

    public GameServiceTest() throws DataAccessException {
    }

    @BeforeEach
    void start() throws DataAccessException {
        initializeDAOs();
        authToken = authDAO.createAuth("nick").authToken();
        populateGamesDirectly();
    }

    @DisplayName("Populate games for game service tests")
    void populateGamesDirectly() throws DataAccessException {
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

    @Test
    @DisplayName("Properly retrieve list of games")
    void listGamesPositive() throws DataAccessException{
        HashSet<GameData> games = new GameService(userDAO, gameDAO, authDAO).listGames(authToken);

        Assertions.assertEquals(populatedGames, games);
    }

    @Test
    @DisplayName("Retrieve games with invalid authToken")
    void listGamesNegative(){
        DataAccessException e = new DataAccessException(401, "Error: unauthorized");
        String invalidAuthToken = "thisTokenDoesntExistInAuthDB";

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> new GameService(userDAO, gameDAO, authDAO).listGames(invalidAuthToken), "Should not retrieve games with invalid authToken");

        Assertions.assertEquals(e.getStatusCode(), thrown.getStatusCode());
        Assertions.assertEquals(e.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Properly create a new game")
    void createGamePositive() throws DataAccessException {
        int gameID = new GameService(userDAO, gameDAO, authDAO).createGame("A good game", authToken);

        Assertions.assertEquals(gameID, gameDAO.getGame(gameID).gameID());
    }

    @Test
    @DisplayName("Create a game without a name or valid authToken")
    void createGameNegative() {
        DataAccessException eU = new DataAccessException(401, "Error: unauthorized");
        DataAccessException eBR = new DataAccessException(400, "Error: bad request");
        String invalidAuthToken = "thisTokenDoesntExistInAuthDB";

        DataAccessException unauthorized = assertThrows(DataAccessException.class, () -> new GameService(userDAO, gameDAO, authDAO).createGame("A good game", invalidAuthToken), "Should not create game with invalid authToken");
        DataAccessException badRequest = assertThrows(DataAccessException.class, () -> new GameService(userDAO, gameDAO, authDAO).createGame(null, authToken), "Should not create game without game name");

        Assertions.assertEquals(eU.getStatusCode(), unauthorized.getStatusCode());
        Assertions.assertEquals(eU.getMessage(), unauthorized.getMessage());
        Assertions.assertEquals(eBR.getStatusCode(), badRequest.getStatusCode());
        Assertions.assertEquals(eBR.getMessage(), badRequest.getMessage());
    }

    @Test
    @DisplayName("Properly join a game")
    void joinGamePositive() throws DataAccessException{
        int newGameID = gameDAO.createGame("A good game");

        new GameService(userDAO, gameDAO, authDAO).joinGame(newGameID, "WHITE", authToken);

        Assertions.assertNotNull(gameDAO.getGame(newGameID));
        Assertions.assertEquals(newGameID, gameDAO.getGame(newGameID).gameID());

        AuthData newPlayerData = authDAO.createAuth("A new player");
        new GameService(userDAO, gameDAO, authDAO).joinGame(newGameID, "BLACK", newPlayerData.authToken());

        Assertions.assertEquals(newPlayerData.username(), gameDAO.getGame(newGameID).blackUsername());

    }

    @Test
    @DisplayName("Join a nonexistent game or join as a claimed team color")
    void joinGameNegative() throws DataAccessException {
        DataAccessException eAT = new DataAccessException(403, "Error: already taken");
        DataAccessException eBR = new DataAccessException(400, "Error: bad request");
        int fakeGameID = -42069;

        DataAccessException badRequest = assertThrows(DataAccessException.class, () -> new GameService(userDAO, gameDAO, authDAO).joinGame(-42069, "WHITE", authToken), "Should not join nonexistent game");

        int newGameID = gameDAO.createGame("A game");
        new GameService(userDAO, gameDAO, authDAO).joinGame(newGameID, "WHITE", authToken);
        AuthData newPlayerData = authDAO.createAuth("A new player");

        DataAccessException alreadyTaken = assertThrows(DataAccessException.class, () -> new GameService(userDAO, gameDAO, authDAO).joinGame(newGameID, "WHITE", newPlayerData.authToken()), "Should not join nonexistent game");

        Assertions.assertEquals(eBR.getStatusCode(), badRequest.getStatusCode());
        Assertions.assertEquals(eBR.getMessage(), badRequest.getMessage());
        Assertions.assertEquals(eAT.getStatusCode(), alreadyTaken.getStatusCode());
        Assertions.assertEquals(eAT.getMessage(), alreadyTaken.getMessage());

    }


}
