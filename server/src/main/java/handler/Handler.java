package handler;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public interface Handler {
    public Object deserializeRequest(Request req);
    public Object serializeResponse(Object res);
    public Object handle(Request req, Response res) throws DataAccessException;
    public Object performService(Object reqObject, Request res) throws DataAccessException;
    public Class requestClass(Request req);
}
