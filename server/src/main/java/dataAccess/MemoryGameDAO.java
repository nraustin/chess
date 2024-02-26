package dataAccess;

import model.GameData;
import model.UserData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private final HashSet<GameData> gameDB = new HashSet<>();
    public void createGame(GameData game){

    }
    public GameData getGame(int gameID) throws DataAccessException{
        for(GameData gameData: gameDB){
            if(gameID == gameData.gameID()){
                return gameData;
            }
        }
        return null;
    }

    public HashSet<GameData> listGames(){
        return gameDB;
    }
    public void updateGame(GameData game){

    }
    public void clearData(){
        gameDB.clear();
    }

}
