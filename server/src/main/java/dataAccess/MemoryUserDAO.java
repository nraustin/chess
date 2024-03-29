package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    private final HashSet<UserData> userDB = new HashSet<>();
    public void createUser(UserData user) {
        userDB.add(user);
    }
    public UserData getUser(String username) {
        for(UserData user: userDB){
            if(username.equals(user.username())){
                return user;
            }
        }
        return null;
    }

    public void clearData(){
        userDB.clear();
    }

    public boolean verifyUser(String password, String providedPassword){
        return false;
    }

}
