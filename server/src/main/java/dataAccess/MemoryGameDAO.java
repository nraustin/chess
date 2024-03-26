package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.HashSet;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{

    private final HashSet<GameData> gameDB = new HashSet<>();

    public GameData createGame(String gameName) {
        GameData game = new GameData(new Random().nextInt(42069), null, null, gameName, new ChessGame());
        gameDB.add(game);

        return game;
    }

    public GameData getGame(int gameID) {
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

    public void updateGame(GameData alteredGame){
        GameData targetGame = getGame(alteredGame.gameID());
        gameDB.remove(targetGame);
        gameDB.add(alteredGame);
    }

    public void clearData(){
        gameDB.clear();
    }

}
