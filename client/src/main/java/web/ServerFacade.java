package web;

import com.google.gson.Gson;
import ui.EscapeSequences;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

import exception.ResponseException;

public class ServerFacade {

    private final String serverURL;

    public ServerFacade(String serverURL){
        this.serverURL = serverURL;
    }

    private <T> T httpHandler(String method, String endpoint, Object request, Class<T> resClass) throws ResponseException {
        try {
            // 1 create URL instance
            URL url = new URI(serverURL + endpoint).toURL();
            // 2 open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // set request method
            connection.setRequestMethod(method);
            // post request
            if(request != null){
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                writeReqBody(os, request);
            }

        } catch (Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void writeReqBody(OutputStream os, Object request) throws IOException {
        String req = new Gson().toJson(request);
        os.write(req.getBytes());
    }
}
