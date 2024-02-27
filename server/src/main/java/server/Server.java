package server;

import dataAccess.*;

import handler.*;
import spark.*;

public class Server {

    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public Server() {
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
    }

    public static void main(String[] args){
        Server server = new Server();
        server.run(8080);
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Configure handlers
        InitializeHandler initializeHandler = new InitializeHandler(userDAO, gameDAO, authDAO);
        RegisterHandler registerHandler = new RegisterHandler(userDAO, gameDAO, authDAO);
        SessionHandler sessionHandler = new SessionHandler(userDAO, gameDAO, authDAO);
        GameHandler gameHandler = new GameHandler(userDAO, gameDAO, authDAO);

        // Initialize routes
        Spark.delete("/db", initializeHandler::handle);
        Spark.post("/user", registerHandler::handle);
        Spark.post("/session", sessionHandler::handle);
        Spark.delete("/session", sessionHandler::handle);
        Spark.get("/game", gameHandler::handle);
        Spark.post("/game", gameHandler::handle);
        Spark.put("/game", gameHandler::handle);

        // Handle exceptions
        handleErrors();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void handleErrors(){
        Spark.exception(DataAccessException.class, (e, req, res) -> new ErrorHandler().handleDataAccessException(e, req, res));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
