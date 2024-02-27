package dataAccess;

import model.UserData;
public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;
    public UserData getUser(String username);
    public void clearData();
}
