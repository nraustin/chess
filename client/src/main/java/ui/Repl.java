package ui;

import web.ChessClient;
import web.websocket.NotificationHandler;

import javax.management.Notification;
import java.util.Scanner;

public class Repl implements NotificationHandler {

    private final ChessClient client;

    public Repl(String serverURL) {
        client = new ChessClient(serverURL, this);
    }

    public void run() {
        System.out.println(String.format("%s Welcome to Chess %s \n Please login or register to continue.", EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_QUEEN));

        Scanner scanner = new Scanner(System.in);
        String res = "";
        while(!res.equals("Goodbye!")){
            printPrompt();
            String line = scanner.nextLine();
            try{
                res = client.eval(line);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + res);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_TEXT_COLOR_WHITE + client.getState() + " >>> " + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
    }

    public void notify(String notification){
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + notification);
        printPrompt();
    }
}
