package dataAccess;

import dataAccess.exception.DataAccessException;
import model.UserData;

import java.util.HashSet;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;
    public UserData getUser(String username);
    public void clearData() throws DataAccessException;
}
