package web;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import ui.PreLoginUI;
import ui.PostLoginUI;
import ui.GameUI;
import ui.State;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChessClient {

    private static ChessClient client;
    private final String serverURL;
    private ServerFacade server;
    private State state = State.LOGGEDOUT;
    private static UserData userData;
    private Map<Integer, GameData> clientGames = new HashMap<>();

    public ChessClient(String serverURL){
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.client = this;
    }

    public static ChessClient getClient(){
        return client;
    }

    public String eval(String input){
        try{
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            switch(state) {
                case LOGGEDOUT -> {
                    return new PreLoginUI().eval(cmd, params);
                }
                case LOGGEDIN -> {
                    server.listGames();
                    return new PostLoginUI().eval(cmd, params);
                }
                case GAMEPLAY -> {
                    return new GameUI().eval(cmd, params);
                }
                default -> {
                    return "Error: state unknown";
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getState(){
        switch (state){
            case LOGGEDOUT -> {
                return "[LOGGED_OUT]";
            }
            case LOGGEDIN -> {
                return "[LOGGED_IN]";
            }
            case GAMEPLAY -> {
                return "[GAMEPLAY]";
            }
            default -> {
                return "Error: state unknown";
            }
        }
    }

    public void setState(State state){
        this.state = state;
    }

    public ServerFacade getServer(){
        return server;
    }

    public UserData getUser(){
        return userData;
    }

    public void setUser(UserData userData){
        this.userData = userData;
    }

    public void clearUser(){
        this.userData = null;
    }

    public void setCurrentGames(Map<Integer, GameData> games){
        this.clientGames = games;
    }

    public Map<Integer, GameData> getCurrentGames(){
        return clientGames;
    }

    public void addGame(GameData gameData){
        int key = clientGames.isEmpty() ? 1 : clientGames.size() + 1;
        clientGames.put(key, gameData);
    }


}
