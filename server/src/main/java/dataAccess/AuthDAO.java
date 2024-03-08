package dataAccess;

import dataAccess.exception.DataAccessException;
import model.AuthData;

import java.util.HashSet;

public interface AuthDAO {
    public AuthData createAuth(String username) throws DataAccessException;
    public AuthData getAuth(String authToken);
    public void deleteAuth(AuthData authToken);
    public void clearData() throws DataAccessException;
}
