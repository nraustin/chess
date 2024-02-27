package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.HashSet;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{

    private final HashSet<GameData> gameDB = new HashSet<>();

    public Integer createGame(String gameName) throws DataAccessException{
        if(gameName == null){
            throw new DataAccessException(400, "Error: bad request");
        }

        GameData game = new GameData(new Random().nextInt(42069), null, null, gameName, new ChessGame());
        gameDB.add(game);

        return game.gameID();
    }

    public GameData getGame(int gameID) throws DataAccessException{
        for(GameData gameData: gameDB){
            if(gameID == gameData.gameID()){
                return gameData;
            }
        }
        throw new DataAccessException(400, "Error: bad request");
    }

    public HashSet<GameData> listGames(){
        return gameDB;
    }

    public void updateGame(GameData joinedGame) throws DataAccessException{
        GameData targetGame = getGame(joinedGame.gameID());
        gameDB.remove(targetGame);
        gameDB.add(joinedGame);
    }

    public void clearData(){
        gameDB.clear();
    }

}
