package dataAccess.sql;

import dataAccess.AuthDAO;
import dataAccess.exception.DataAccessException;
import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO extends BaseSQLDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        this.configureDatabase(createTableStatement);
    }

    private String[] createTableStatement = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `username` varchar(128) NOT NULL,
              `authToken` varchar(128) NOT NULL,
              PRIMARY KEY(username)
            ) 
            """
    };

    private AuthData generateAuthToken(String username) {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        return authData;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData authToken = generateAuthToken(username);
        String statement = "INSERT INTO auth (username, authToken) VALUES (?, ?);";
        executeUpdate(statement, username, authToken);
        return null;
    }

    public AuthData getAuth(String authToken) {
        return null;
    }

    public void deleteAuth(AuthData authToken) {
        return;
    }

    public void clearData() throws DataAccessException {
        executeUpdate("DELETE FROM auth;");
    }

}
