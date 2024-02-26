package dataAccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    private final HashSet<UserData> users = new HashSet<>();
    public void createUser(UserData user) throws DataAccessException {
        if(users.contains(user)){
            throw new DataAccessException("User already exists");
        }
        users.add(user);
    }
    public UserData getUser(String username) {
        for(UserData user: users){
            if(username == user.username()){
                return user;
            }
        }
        return null;
    }

    public void clearData() throws DataAccessException{
        users.clear();
    }
}
