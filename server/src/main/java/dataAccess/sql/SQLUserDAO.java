package dataAccess.sql;

import dataAccess.UserDAO;
import dataAccess.exception.DataAccessException;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SQLUserDAO extends BaseSQLDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        this.configureDatabase(createTableStatement);
    }

    private String[] createTableStatement = {
            """
            CREATE TABLE IF NOT EXISTS user (
              `username` varchar(128) NOT NULL,
              `password` varchar(128) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY(username)
            ) 
            """
    };

    public void createUser(UserData user) throws DataAccessException {
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?);";
        executeUpdate(statement, user.username(), encryptedPassword, user.email());
    }

    public UserData getUser(String username){
        return null;
    }

    public void clearData() throws DataAccessException {
        executeUpdate("DELETE FROM user;");
    }
}
