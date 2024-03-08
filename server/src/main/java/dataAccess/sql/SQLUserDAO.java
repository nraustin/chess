package dataAccess.sql;

import dataAccess.UserDAO;
import model.UserData;

public class SQLUserDAO extends BaseSQLDAO implements UserDAO {

    public SQLUserDAO(){}

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

    public void createUser(UserData user){
        return;
    }

    public UserData getUser(String username){
        return null;
    }

    public void clearData(){
        return;
    }
}
