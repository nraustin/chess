package dataAccess.sql;

import dataAccess.AuthDAO;
import dataAccess.exception.DataAccessException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO extends BaseSQLDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        this.configureDatabase(createTableStatement);
    }

    private String[] createTableStatement = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(128) NOT NULL,
              `username` varchar(128) NOT NULL,
              PRIMARY KEY (authToken)
            ) 
            """
    };

    public AuthData generateAuthToken(String username) {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        return authData;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData authData = generateAuthToken(username);
        String statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        update(statement, authData.authToken(), username);

        return authData;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        String sqlStatement = "SELECT * FROM auth WHERE authToken = ?";
        AuthData authData = query(sqlStatement,
                resultSet -> {
                    try {
                        if(resultSet.next()){
                            return new AuthData(resultSet.getString("authToken"),
                                                resultSet.getString("username"));
                        } else{
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }, authToken);
        return authData;
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        String sqlStatement = "DELETE FROM auth WHERE authToken = ?";
        update(sqlStatement, authData.authToken());
    }

    public void clearData() throws DataAccessException {
        update("DELETE FROM auth");
    }

}
