package dataAccess;

import dataAccess.exception.DataAccessException;
import model.AuthData;

import java.util.HashSet;

public interface AuthDAO {
    public AuthData createAuth(String username) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(AuthData authToken) throws DataAccessException;
    public void clearData() throws DataAccessException;
}
