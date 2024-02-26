package dataAccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    private final HashSet<UserData> userDB = new HashSet<>();
    public void createUser(UserData user) throws DataAccessException {
        if(userDB.contains(user)){
            throw new DataAccessException("User already exists");
        }
        userDB.add(user);
    }
    public UserData getUser(String username) {
        for(UserData user: userDB){
            if(username == user.username()){
                return user;
            }
        }
        return null;
    }

    public void clearData() throws DataAccessException{
        userDB.clear();
    }
}
