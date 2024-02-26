package dataAccess;

import model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private static HashSet<AuthData> authDB = new HashSet<AuthData>();
    private AuthData generateAuthToken(String username) {
        AuthData authToken = new AuthData(UUID.randomUUID().toString(), username);
        return authToken;
    }
    public AuthData createAuth(String username) {
        AuthData authToken = generateAuthToken(username);
        authDB.add(authToken);
        return authToken;
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        for (AuthData authData: authDB){
            if(authToken == authData.authToken()){
                return authData;
            }
        }

        throw new DataAccessException("Authorization not found");
    }
    public void deleteAuth(String authToken) {
        authDB.remove(authToken);
    }

    public void clearData(){
        authDB.clear();
    }
}
