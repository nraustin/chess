package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    private final ConcurrentHashMap<Integer, Map<String, Session>> connections = new ConcurrentHashMap<>();

    public void addSessionToGame(int gameID, String authToken, Session session){
        if(!connections.containsKey(gameID)){
            connections.put(gameID, new HashMap<>());
        }
        connections.get(gameID).put(authToken, session);
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session){
        if(connections.containsKey(gameID)){
            connections.get(gameID).remove(authToken);
        }
    }

    public void removeSession(Session session){
        for(Map<String, Session> connection: connections.values()){
            for(Map.Entry<String, Session> entry: connection.entrySet()){
                if(entry.getValue().equals(session)){
                    connection.remove(entry);
                }
            }
        }
    }

    public Map<String, Session> getSessionsForGame(int gameID){
        return connections.get(gameID);
    }
}
