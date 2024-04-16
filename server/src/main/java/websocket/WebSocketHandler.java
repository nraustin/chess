package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.exception.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

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

            }
            case LEAVE -> {

            }
            case RESIGN -> {

            }
        }
    }

    private AuthData initializeSession(Session session, JoinPlayerCommand command) throws IOException{
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        try {
            AuthData authData = authDAO.getAuth(authToken);
            GameData gameData = gameDAO.getGame(gameID);
            if(authData == null){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid authToken"));
            }
            if(gameData == null){
                sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: could not find game with gameID"));
            }

            sessions.addSessionToGame(gameID, authToken, session);
            ServerMessage message = new ServerMessage(gameData.game());
            sendMessage(gameID, message, authToken);

            return authData;

        } catch (DataAccessException e) {
            sendError(session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }

        return null;
    }

    private void joinPlayer(Session session, JoinPlayerCommand command) throws IOException {
        AuthData authData = initializeSession(session, command);
        if(authData != null){
            String authToken = command.getAuthString();
            int gameID = command.getGameID();
            String notification = String.format("%s has joined as team %s", authData.username(), command.getColor() == ChessGame.TeamColor.WHITE ? "WHITE" : "BLACK");
            broadcast(authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification), gameID);
        }
    }

    private void joinObserver(Session session, JoinPlayerCommand command) throws IOException {
        AuthData authData = initializeSession(session, command);
        if(authData != null) {
            String authToken = command.getAuthString();
            int gameID = command.getGameID();
            String notification = String.format("%s is watching", authData.username());
            broadcast(authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification), gameID);
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
        ArrayList<Session> removeList = new ArrayList<>();

        for(Map.Entry<String, Session> entry: sMap.entrySet()){
            Session session = entry.getValue();
            String authToken = entry.getKey();
            if(session.isOpen()){
                if(!authToken.equals(excludeAuth)){
                    sendMessage(gameID, message, authToken);
                }
            } else {
                removeList.add(session);
            }
        }

        for(Session closedS: removeList){
            sessions.removeSession(closedS);
        }
    }



}
