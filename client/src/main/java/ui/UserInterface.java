package ui;

import exception.ResponseException;

public interface UserInterface {

    public String eval(String cmd, String[] params) throws ResponseException;
}
