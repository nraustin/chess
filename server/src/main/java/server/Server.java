package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;

import handler.InitializeHandler;
import handler.RegisterHandler;
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


        // Register your endpoints and handle exceptions here.
        InitializeHandler initializeHandler = new InitializeHandler(userDAO, gameDAO, authDAO);
        RegisterHandler registerHandler = new RegisterHandler(userDAO, gameDAO, authDAO);

        // Initialize routes
        Spark.delete("/db", initializeHandler::handle);
        Spark.post("/user", registerHandler::handle);

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
