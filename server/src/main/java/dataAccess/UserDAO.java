package dataAccess;

import dataAccess.exception.DataAccessException;
import model.UserData;

import java.util.HashSet;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void clearData() throws DataAccessException;
    public boolean verifyUser(String password, String providedPassword) throws DataAccessException;
}
