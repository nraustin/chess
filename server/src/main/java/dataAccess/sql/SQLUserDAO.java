package dataAccess.sql;

import dataAccess.UserDAO;
import dataAccess.exception.DataAccessException;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;

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

    public boolean verifyUser(String password, String providedPassword){
        return new BCryptPasswordEncoder().matches(password, providedPassword);
    }

    public void createUser(UserData user) throws DataAccessException {
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?);";
        update(statement, user.username(), encryptedPassword, user.email());
    }

    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT * FROM user WHERE username = ?";
        UserData userData = query(statement,
                resultSet -> {
                    try {
                        if(resultSet.next()){
                            System.out.println(String.format("from getUserSQL rs %s", resultSet.getString("username")));

                            return new UserData(resultSet.getString("username"),
                                                resultSet.getString("password"),
                                                resultSet.getString("email"));
                        } else{
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, username);
        System.out.println(String.format("from getUserSQL %s", username));
        return userData;
    }

    public void clearData() throws DataAccessException {
        update("DELETE FROM user;");
    }
}
