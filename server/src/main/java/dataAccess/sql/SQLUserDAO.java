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
              PRIMARY KEY (username)
            ) 
            """
    };

    public boolean verifyUser(String username, String providedPassword) throws DataAccessException {
        String sqlStatement = "SELECT password FROM user WHERE username = ?";
        String dbPassword = query(sqlStatement,
                resultSet -> {
                    try{
                        if(resultSet.next()){
                            return resultSet.getString("password");
                        }
                        else{
                            return null;
                        }
                    } catch (SQLException e){
                        throw new RuntimeException(e.getMessage());
                    }
                }, username);
        System.out.println(String.format("pwd: %s", dbPassword));
        System.out.println(String.format("match: %s", new BCryptPasswordEncoder().matches(providedPassword, dbPassword)));
        return new BCryptPasswordEncoder().matches(providedPassword, dbPassword);
    }


    public void createUser(UserData user) throws DataAccessException {
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        String sqlStatement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        update(sqlStatement, user.username(), encryptedPassword, user.email());
    }

    public UserData getUser(String username) throws DataAccessException {
        String sqlStatement = "SELECT * FROM user WHERE username = ?";
        UserData userData = query(sqlStatement,
                resultSet -> {
                    try {
                        if(resultSet.next()){
                            return new UserData(resultSet.getString("username"),
                                                resultSet.getString("password"),
                                                resultSet.getString("email"));
                        } else{
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }, username);
        return userData;
    }

    public void clearData() throws DataAccessException {
        update("DELETE FROM user");
    }
}
