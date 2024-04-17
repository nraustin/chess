package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.exception.DataAccessException;
import dataAccess.exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private AuthData authData;
    private GameData gameData;

    private WebSocketSessions sessions = new WebSocketSessions();

    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

//    @OnWebSocketConnect
//    public void onConnect(Session session){}
//    @OnWebSocketClose
//    public void onClose(Session session){}
//    @OnWebSocketError
//    public void onError(Throwable throwable){}


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()){
            case JOIN_PLAYER -> {
                joinPlayer(session, new Gson().fromJson(message, JoinPlayerCommand.class));
            }
            case JOIN_OBSERVER -> {
                joinObserver(session, new Gson().fromJson(message, JoinPlayerCommand.class));
            }
            case MAKE_MOVE -> {
                makeMove(session, new Gson().fromJson(message, ChessMoveCommand.class));
            }
            case LEAVE -> {
                leave(session, new Gson().fromJson(message, LeaveCommand.class));
            }
            case RESIGN -> {
                resign(session, new Gson().fromJson(message, ResignCommand.class));
            }
        }
    }

    private void initialize(Session session, UserGameCommand command, int gameID) throws IOException{
        String authToken = command.getAuthString();

        try {
            authData = authDAO.getAuth(authToken);
            if(authData == null){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid authToken"));
                return;
            }
            gameData = gameDAO.getGame(gameID);
            if(gameData == null){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: could not find game with gameID"));
            }

        } catch (DataAccessException e) {
            sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error:" + e.getMessage()));
        }
    }

    private void joinPlayer(Session session, JoinPlayerCommand command) throws IOException {
        initialize(session, command, command.getGameID());
        if(authData != null && gameData != null){
            String authToken = command.getAuthString();

            int gameID = command.getGameID();

            boolean whiteTaken = gameData.whiteUsername() != null;
            boolean blackTaken = gameData.blackUsername() != null;

            int passes = 0;
            if(command.getColor() == ChessGame.TeamColor.WHITE && whiteTaken && gameData.whiteUsername().equals(authData.username())){
                passes++;
            } else if(command.getColor() == ChessGame.TeamColor.BLACK && blackTaken && gameData.blackUsername().equals(authData.username())){
                passes++;
            }

            if(passes != 1){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: occupied or invalid team"));
                return;
            }

            sessions.addSessionToGame(gameID, authToken, session);
            ServerMessage message = new ServerMessage(gameData.game());
            sendMessage(gameID, message, authToken);

            String notification = String.format("%s has joined as team %s", authData.username(), command.getColor() == ChessGame.TeamColor.WHITE ? "WHITE" : "BLACK");
            broadcast(authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification), gameID);
        }
    }


    private void joinObserver(Session session, JoinPlayerCommand command) throws IOException {
        initialize(session, command, command.getGameID());
        if(authData != null && gameData != null) {
            String authToken = command.getAuthString();
            int gameID = command.getGameID();

            sessions.addSessionToGame(gameID, authToken, session);
            ServerMessage message = new ServerMessage(gameData.game());
            sendMessage(gameID, message, authToken);

            String notification = String.format("%s is watching", authData.username());
            broadcast(authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification), gameID);
        }
    }

    private void makeMove(Session session, ChessMoveCommand command) throws IOException {
        initialize(session, command, command.getGameID());
        if(authData != null && gameData != null){
            String authToken = command.getAuthString();
            int gameID = command.getGameID();
            ChessMove move = command.getMove();
            ChessGame game = gameData.game();

            boolean whiteTeamTurn = game.getTeamTurn() == ChessGame.TeamColor.WHITE;
            boolean blackTeamTurn = game.getTeamTurn() == ChessGame.TeamColor.BLACK;
            String currentPlayer = authData.username();

            if(game.isGameOver()){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: this game has ended"));
                return;
            }

            if(whiteTeamTurn && !gameData.whiteUsername().equals(currentPlayer) || blackTeamTurn && !gameData.blackUsername().equals(currentPlayer)){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: it's not your turn yet"));
                return;
            }

            if(!gameData.whiteUsername().equals(currentPlayer) && !gameData.blackUsername().equals(currentPlayer)){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: you are an observer"));
                return;
            }

            try{
                game.makeMove(move);
                gameDAO.updateGame(gameData);
            } catch (InvalidMoveException | DataAccessException e){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage()));
                return;
            }

            ServerMessage message = new ServerMessage(game);
            sendMessage(gameID, message, authToken);
            broadcast(authToken, message, gameID);

            ServerMessage moveNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s made a move: %s", authData.username(), command.parsedMove()));
            broadcast(authToken, moveNotification, gameID);
            String opponent = authData.username().equals(gameData.whiteUsername()) ? gameData.blackUsername() : gameData.whiteUsername();

            if(game.isInCheck(game.getTeamTurn())){
                ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is check", opponent));
                sendMessage(gameID, notification, authToken);
                broadcast(authToken, notification, gameID);
            }

            if(game.isInCheckmate(game.getTeamTurn())){
                ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is checkmate", opponent));
                game.endGame();
                sendMessage(gameID, notification, authToken);
                broadcast(authToken, notification, gameID);
            }

            if(game.isInStalemate(game.getTeamTurn())){
                ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is in stalemate", opponent));
                game.endGame();
                sendMessage(gameID, notification, authToken);
                broadcast(authToken, notification, gameID);
            }
        }

    }

    private void resign(Session session, ResignCommand command) throws IOException {
        initialize(session, command, command.getGameID());
        if(authData != null && gameData != null) {
            String authToken = command.getAuthString();
            int gameID = command.getGameID();
            String currentUser = authData.username();
            ChessGame game = gameData.game();

            if (game.isGameOver()) {
                System.out.println("c1" + game.isGameOver());
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: game is already over"));
                return;
            }

            if (!gameData.whiteUsername().equals(currentUser) && !gameData.blackUsername().equals(currentUser)) {
                System.out.println("c2");
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: you are an observer"));
                return;
            }

            try {
                game.endGame();
                gameDAO.updateGame(gameData);
            } catch (DataAccessException e) {
                System.out.println("c3");
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage()));
                return;
            }

            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s resigned from the game", currentUser));
            sendMessage(gameID, notification, authToken);
            broadcast(authToken, notification, gameID);
        }
    }

    private void leave(Session session, LeaveCommand command) throws IOException {
        initialize(session, command, command.getGameID());
        if(authData != null && gameData != null){
            String authToken = command.getAuthString();
            int gameID = command.getGameID();

            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s left the game", authData.username()));
            broadcast(authToken, notification, gameID);

            sessions.removeSessionFromGame(gameID, authToken, session);
        }
    }

    private void sendMessage(int gameID, ServerMessage message, String authToken) throws IOException {
        Session session = sessions.getSessionsForGame(gameID).get(authToken);
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void sendError(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void broadcast(String excludeAuth, ServerMessage message, int gameID) throws IOException {
        Map<String, Session> sMap = sessions.getSessionsForGame(gameID);
        if(sMap == null){
            return;
        }
        List<Session> removeList = new ArrayList<>();

        sMap.forEach((authToken, session) -> {
            try {
                if (session.isOpen()) {
                    if (!authToken.equals(excludeAuth)) {
                        sendMessage(gameID, message, authToken);
                    }
                } else {
                    removeList.add(session);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        removeList.forEach(sessions::removeSession);
    }



}
