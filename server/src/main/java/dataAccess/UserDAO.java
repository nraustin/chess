package dataAccess;

import model.UserData;

import java.util.HashSet;

public interface UserDAO {
    public void createUser(UserData user);
    public UserData getUser(String username);
    public void clearData();
    public HashSet<UserData> listUsers();
}
