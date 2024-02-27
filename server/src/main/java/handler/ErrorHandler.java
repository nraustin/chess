package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ErrorHandler {

    public ErrorHandler(){}

    public void handleDataAccessException(DataAccessException e, Request req, Response res){
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        res.body(body);
        res.status(e.getStatusCode());
    }

}
