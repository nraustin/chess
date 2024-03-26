package web;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import ui.PreLoginUI;
import ui.PostLoginUI;
import ui.GameUI;
import ui.State;


import java.util.Arrays;
import java.util.HashSet;

public class ChessClient {

    private static ChessClient client;
    private final String serverURL;
    private ServerFacade server;
    private State state = State.LOGGEDOUT;
    private static UserData userData;
    private ChessGame game;
    private HashSet<GameData> clientGames;

    public ChessClient(String serverURL){
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.client = this;
    }

    // I think this is alright? There should only be one client/state here
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

    public void setState(State state){
        this.state = state;
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

    public void setCurrentGame(ChessGame game){
        this.game = game;
    }

    public ChessGame getCurrentGame(){
        return game;
    }


}
