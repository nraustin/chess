package websocket;

import spark.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    private final ConcurrentHashMap<Integer, Map<String, Session>> connections = new ConcurrentHashMap<>();

    public void addSessionToGame(int gameID, String authToken, Session session){

    }

    public void removeSessionFromGame(int gameID, String authToken, Session session){

    }

    public void removeSession(Session session){

    }

    public Map<String, Session> getSessionsForGame(int gameID){

    }
}
