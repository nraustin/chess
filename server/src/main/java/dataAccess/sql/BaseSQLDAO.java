package dataAccess.sql;

import dataAccess.DatabaseManager;
import dataAccess.exception.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public abstract class BaseSQLDAO {

    public BaseSQLDAO() {}

    protected int update(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    // Universal logic for db updates
                    var param = params[i];

                    if(param instanceof String p){
                        ps.setString(i + 1, p);
                    }
                    else if(param instanceof Integer p){
                        ps.setInt(i+ 1, p);
                    }
                    else if(param == null){
                        ps.setNull(i+ 1, NULL);
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println(rs.getInt(1));
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    protected <D> D query(String statement, Function<ResultSet, D> rsHandler, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {
                    // Universal logic for db queries
                    var param = params[i];

                    if(param instanceof String p){
                        ps.setString(i + 1, p);
                    }
                    else if(param instanceof Integer p){
                        ps.setInt(i+ 1, p);
                    }
                    else if(param == null){
                        ps.setNull(i+ 1, NULL);
                    }
                }
                var rs = ps.executeQuery();
                return rsHandler.apply(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }


    protected void configureDatabase(String[] createTableStatements) throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createTableStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
