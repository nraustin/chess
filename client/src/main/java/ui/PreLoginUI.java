package ui;


public class PreLoginUI implements UserInterface{

    public String eval(String cmd, String[] params){
        switch(cmd){
            case "help" -> {
//                return help();
            }
            case "quit" -> {
//                return quit();
            }
            case "login" -> {
//                return login();
            }
            case "register" -> {
//                return register();
            }
            default -> {
//                return help();
            }
        }
        return "not implemented";
    }
}
