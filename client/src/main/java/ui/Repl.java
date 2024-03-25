package ui;

import web.ChessClient;

import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    public Repl(String serverURL) {
        client = new ChessClient(serverURL);
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
}
