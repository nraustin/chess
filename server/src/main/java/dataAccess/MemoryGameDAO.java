package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{

    private final HashSet<GameData> gameDB = new HashSet<>();

    public Integer createGame(String gameName) throws DataAccessException{
        if(gameName == null){
            throw new DataAccessException(400, "Error: bad request");
        }

        GameData game = new GameData(42069, null, null, gameName, new ChessGame());
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

    public void updateGame(String username, GameData game, String playerColor, String otherTeamUsername){
//        if(playerColor.equals("WHITE")){
//            game = new GameData(game.gameID(), username, opposingTeamUsername, game.gameName(), game.game());
//        } else if(playerColor.equals("BLACK")){
//            game = new GameData(game.gameID(), opposingTeamUsername, username, game.gameName(), game.game());
//        }
        game = new GameData(game.gameID(), username, otherTeamUsername, game.gameName(), game.game());
    }

    public void clearData(){
        gameDB.clear();
    }

}
