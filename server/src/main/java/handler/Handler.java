package handler;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public interface Handler<T> {
    public Object deserializeRequest(Request req);
    public Object serializeResponse(Object res);
    public Object handle(Request req, Response res);
    public Object performService(T req);
    public Class<T> requestClass();
}
