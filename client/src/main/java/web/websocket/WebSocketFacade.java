package web.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import ui.ChessBoardPrinter;
import ui.EscapeSequences;
import web.ChessClient;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {

    private Session session;
    private NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            System.out.println(socketURI);
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(this);

        } catch (URISyntaxException | DeploymentException | IOException e){
            e.printStackTrace();

            throw new ResponseException(500, e.getMessage());
        }
    }

    public void joinPlayer() throws IOException {
        UserGameCommand command = new JoinPlayerCommand(ChessClient.getClient().getServer().getAuthToken(), ChessClient.getClient().getCurrentGame().gameID(), ChessClient.getClient().getPlayerColor());
        send(command);
    }

    public void joinObserver(){

    }

    public void makeMove(){

    }

    public void leaveGame(){

    }

    public void resignGame(){

    }

    private void send(UserGameCommand command) throws IOException {
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    @Override
    public void onMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch(serverMessage.getServerMessageType()){
            case LOAD_GAME -> {
                notificationHandler.notify(ChessBoardPrinter.printBoard(serverMessage.getGame(), ChessClient.getClient().getPlayerColor()));
            }
            case ERROR, NOTIFICATION -> {
                notificationHandler.notify(EscapeSequences.SET_TEXT_COLOR_RED + serverMessage.getMessage());
            }
        }
    }




}
