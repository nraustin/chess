package web;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import ui.EscapeSequences;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

import exception.ResponseException;

public class ServerFacade {

    private final String serverURL;
    private String authToken = null;

    public ServerFacade(String serverURL){
        this.serverURL = serverURL;
    }

    public void login(UserData userData) throws ResponseException {
        AuthData authData = httpHandler("POST", "/session", userData, AuthData.class);
        authToken = authData.authToken();
    }

    public void register(UserData userData) throws ResponseException {
        AuthData authData = httpHandler("POST", "/user", userData, AuthData.class);
        authToken = authData.authToken();
    }

    public void logout() throws ResponseException {
        httpHandler("DELETE", "/session", null, null);
    }

    private <T> T httpHandler(String method, String endpoint, Object request, Class<T> resClass) throws ResponseException {
        try {
            URL url = new URI(serverURL + endpoint).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(request == null ? false : true);

            if(authToken != null){
                connection.addRequestProperty("Authorization", authToken);
            }

            if(request != null){
                connection.addRequestProperty("Content-Type", "application/json");
                OutputStream os = connection.getOutputStream();
                writeReqBody(os, request);
            }

            connection.connect();

            int status = connection.getResponseCode();
            if(status != 200){
                throw new ResponseException(status, String.format("Failure: %s: %s", status, connection.getResponseMessage()));
            }

            return readResBody(connection, resClass);
        } catch (Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void writeReqBody(OutputStream os, Object request) throws IOException {
        String reqData = new Gson().toJson(request);
        os.write(reqData.getBytes());
    }

    private static <T> T readResBody (HttpURLConnection connection, Class<T> resClass) throws IOException {
        T res = null;
        if(connection.getContentLength() < 0){
            try (InputStream resBody = connection.getInputStream()){
                InputStreamReader reader = new InputStreamReader(resBody);
                if(resClass != null) {
                    res = new Gson().fromJson(reader, resClass);
                }
            }
        }
        return res;
    }


}
