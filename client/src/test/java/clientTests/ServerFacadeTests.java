package clientTests;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.JoinGameRequest;
import server.Server;
import web.ChessClient;
import web.ServerFacade;

import java.rmi.ServerError;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    private static String serverURL;

    private String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverURL = String.format("http://localhost:%d", port);
        serverFacade = new ServerFacade(serverURL);
    }

    @BeforeEach
    void clearData() throws ResponseException {
        serverFacade.clearData();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() throws ResponseException {
        UserData userData = new UserData("nick", "issleepdeprived", "yeah");
        serverFacade.register(userData);
        authToken = serverFacade.getAuthToken();

        Assertions.assertNotNull(authToken);
    }

    @Test
    public void registerNegative() throws ResponseException {
        UserData userData = new UserData(null, "issleepdeprived", "yeah");

        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(userData));
    }

    @Test
    public void loginPositive() throws ResponseException {
        UserData userData = new UserData("nick", "issleepdeprived", "yeah");
        serverFacade.register(userData);
        String authTokenPrev = serverFacade.getAuthToken();
        serverFacade.login(userData);
        String authTokenCurr = serverFacade.getAuthToken();

        Assertions.assertNotNull(authTokenCurr);
        Assertions.assertNotEquals(authTokenPrev, authTokenCurr);
    }

    @Test
    public void logoutPositive() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> registerPositive());
        serverFacade.logout();
        Assertions.assertNull(serverFacade.getAuthToken());
    }

    @Test
    public void logoutNegative() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout());
    }

    @Test
    public void createGamePositive() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> registerPositive());
        GameData gameData = new GameData(42, null, null, "ggnore", null);
        Assertions.assertNotNull(serverFacade.createGame(gameData));
    }

    @Test
    public void createGameNegative() {
        GameData gameData = new GameData(42, null, null, null, null);
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.createGame(gameData));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        ChessClient client = new ChessClient(serverURL);
        UserData userData = new UserData("nick", "issleepdeprived", "yeah");
        Assertions.assertDoesNotThrow(() -> client.getServer().register(userData));

        GameData gameData = new GameData(42, null, null, "ggnore", null);
        client.getServer().createGame(gameData);
        Assertions.assertDoesNotThrow(() -> client.getServer().listGames());

        Assertions.assertNotNull(client.getCurrentGames());
    }

    @Test
    public void listGameNegative(){
        ChessClient client = new ChessClient(serverURL);
        UserData userData = new UserData("nick", "issleepdeprived", "yeah");
        Assertions.assertDoesNotThrow(() -> client.getServer().register(userData));

        Assertions.assertDoesNotThrow(() -> client.getServer().listGames());

        Assertions.assertTrue(client.getCurrentGames().isEmpty());
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> registerPositive());
        GameData gameData = new GameData(42, null, null, "ggnore", null);
        int gameID = serverFacade.createGame(gameData);
        Assertions.assertNotNull(gameID);

        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameID);
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(joinRequest));
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(new JoinGameRequest("notacolor", 0)));
    }

}
