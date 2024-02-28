package dataAccess;

import model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private static HashSet<AuthData> authDB = new HashSet<AuthData>();
    private AuthData generateAuthToken(String username) {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        return authData;
    }
    public AuthData createAuth(String username) {
        AuthData authToken = generateAuthToken(username);
        authDB.add(authToken);
        return authToken;
    }

    public AuthData getAuth(String authToken) {
        if(authToken != null) {
            for (AuthData authData : authDB) {
                if (authToken.equals(authData.authToken())) {
                    return authData;
                }
            }
        }
        return null;
    }
    public void deleteAuth(AuthData authToken) {
        authDB.remove(authToken);
    }

    public void clearData(){
        authDB.clear();
    }

}
