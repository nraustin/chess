package handler;

import dataAccess.DataAccessException;

public interface Handler<T> {
    public Object performService(String authToken, T req);
    public Class<T> retrieveRequestClass();
}
