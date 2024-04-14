package websocket;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import handler.BaseHandler;
import org.eclipse.jetty.websocket.api.annotations.*;
import spark.Session;
import webSocketMessages.userCommands.UserGameCommand;

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

    @OnWebSocketConnect
    public void onConnect(Session session){}
    @OnWebSocketClose
    public void onClose(Session session){}
    @OnWebSocketError
    public void onError(Throwable throwable){}


    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

    }






}
