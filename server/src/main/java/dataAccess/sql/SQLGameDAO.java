package dataAccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.GameDAO;
import dataAccess.exception.DataAccessException;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;

public class SQLGameDAO extends BaseSQLDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        this.configureDatabase(createTableStatement);
    }

    private String[] createTableStatement = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(128) DEFAULT NULL,
            `blackUsername` varchar(128) DEFAULT NULL,
            `gameName` varchar(128) NOT NULL UNIQUE,
            `game` longtext NOT NULL,
            PRIMARY KEY (gameID)
            )
            """
    };

    public GameData createGame(String gameName) throws DataAccessException {
        int gameID = new Random().nextInt(42069);
        String sqlStatement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        update(sqlStatement, gameID, null, null, gameName, new Gson().toJson(new ChessGame()));
        return new GameData(gameID, null, null, gameName, new ChessGame());
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String sqlStatement = "SELECT * FROM game WHERE gameID = ?";
        GameData game = query(sqlStatement,
                resultSet -> {
                    try{
                        if(resultSet.next()) {
                            return new GameData(resultSet.getInt("gameID"),
                                                resultSet.getString("whiteUsername"),
                                                resultSet.getString("blackUsername"),
                                                resultSet.getString("gameName"),
                                                new Gson().fromJson(resultSet.getString("game"), ChessGame.class));
                        } else {
                            return null;
                        }

                    } catch (SQLException e){
                        throw new RuntimeException(e.getMessage());
                    }
                }, gameID);
        return game;
    }

    public HashSet<GameData> listGames() throws DataAccessException {
        String sqlStatement = "SELECT * FROM game";
        HashSet<GameData> gameList = query(sqlStatement,
                resultSet -> {
                    HashSet<GameData> games = new HashSet<>();
                    try{
                        while(resultSet.next()) {
                            games.add(new GameData(resultSet.getInt("gameID"),
                                    resultSet.getString("whiteUsername"),
                                    resultSet.getString("blackUsername"),
                                    resultSet.getString("gameName"),
                                    new Gson().fromJson(resultSet.getString("game"), ChessGame.class)));
                        }
                        return games;
                    } catch (SQLException e){
                        throw new RuntimeException(e.getMessage());
                    }
                });
        return gameList;
    }
    public void updateGame(GameData alteredGame) throws DataAccessException {
        String sqlStatement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        update(sqlStatement, alteredGame.whiteUsername(), alteredGame.blackUsername(), alteredGame.gameName(), new Gson().toJson(alteredGame.game()), alteredGame.gameID());
    }

    public void clearData() throws DataAccessException {
        update("DELETE FROM game");
    }

}
