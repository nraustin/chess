package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String username);
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(AuthData authToken);
    public void clearData();
}
