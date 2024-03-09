package dataAccessTests;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.exception.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLGameDAOTest extends  SQLDAOTest{

    private UserData testUser;
    private String authToken;
    private GameData testGame;

    public  SQLGameDAOTest() throws DataAccessException{

    }

    @BeforeEach
    void start() throws DataAccessException {
        initializeDAOs();
        authToken = authDAO.createAuth("nick").authToken();
        testUser = new UserData("nick", "issuper", "coolandhotmail.com");
        testGame = new GameData(42069, null, null, "that's not my name", new ChessGame());
    }

    @Test
    @DisplayName("Properly create a game")
    void createGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame(testGame.gameName());

        Assertions.assertEquals(gameID, gameDAO.getGame(gameID).gameID());
    }

    @Test
    @DisplayName("Create a game with the same name")
    void createGameNegative() throws DataAccessException {
        DataAccessException e = new DataAccessException(500, "unable to update database: INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?), Duplicate entry 'that's not my name' for key 'game.gameName'");

        createGamePositive();
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> gameDAO.createGame("that's not my name"));

        Assertions.assertEquals(e.getStatusCode(), thrown.getStatusCode());
        Assertions.assertEquals(e.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Properly retrieve a game")
    void getGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame(testGame.gameName());

        Assertions.assertEquals(testGame.gameName(), gameDAO.getGame(gameID).gameName());
    }

    @Test
    @DisplayName("Retrieve a nonexistent game")
    void getGameNegative() throws DataAccessException {
        int gameID = 42070;

        Assertions.assertNull(gameDAO.getGame(gameID));
    }


    @Test
    @DisplayName("List all games")
    void listGamesPositive() throws DataAccessException {
        HashSet<Integer> gameIDs = new HashSet<>();

        int game1 = gameDAO.createGame("they call me Stacy");
        int game2 = gameDAO.createGame("they call me her");
        int game3 = gameDAO.createGame("they call me Jane");

        gameIDs.add(game1);
        gameIDs.add(game2);
        gameIDs.add(game3);

        HashSet<GameData> listedGames = gameDAO.listGames();
        HashSet<Integer> listedGameIDs = new HashSet<>();

        for(GameData game : listedGames){
            listedGameIDs.add(game.gameID());
        }

        for(Integer gameID : gameIDs){
            Assertions.assertTrue(listedGameIDs.contains(gameID));
        }
    }

    @Test
    @DisplayName("List games when none exist")
    void listGamesNegative() throws DataAccessException {
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
    }


    @Test
    @DisplayName("Properly update a game with a new player")
    void updateGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame(testGame.gameName());

        GameData updatedGame = new GameData(gameID, "frisbee golf", null, "that's not my name", new ChessGame());
        gameDAO.updateGame(updatedGame);

        Assertions.assertEquals(updatedGame.whiteUsername(), gameDAO.getGame(gameID).whiteUsername());
    }

    @Test
    @DisplayName("Update a game with no changes")
    void updateGameNegative() throws DataAccessException {
        int gameID = gameDAO.createGame(testGame.gameName());
        GameData game = gameDAO.getGame(gameID);

        gameDAO.updateGame(game);

        Assertions.assertEquals(game, gameDAO.getGame(gameID));
    }

    @Test
    @DisplayName("Clear game table")
    void clearGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame(testGame.gameName());
        gameDAO.clearData();

        Assertions.assertNull(gameDAO.getGame(gameID));
    }
}
