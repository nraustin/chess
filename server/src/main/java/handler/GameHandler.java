package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import response.GameResponse;
import service.GameService;
import spark.Request;

import java.util.HashSet;

public class GameHandler extends BaseHandler<GameData>{
    public GameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public Object performService(GameData reqObject, Request req) throws DataAccessException {
        GameService service = new GameService(userDAO, gameDAO, authDAO);

        System.out.println("Anything here?");
        switch (req.requestMethod()){
            case "GET":
                return listGamesService(service, req);
            case "POST":
                Object res = createGameService(reqObject.gameName(), service, req);
                System.out.println(String.format("in performService: %s", res));
                return res;
//            case "PUT":
//                return joinGameService(service, req);
            default:
                throw new DataAccessException(401, "Error: unsupported request method");
        }
    }

    public Object listGamesService(GameService service, Request req) throws DataAccessException {
        HashSet<GameData> games = service.listGames(req.headers("Authorization"));
        GameResponse listGamesRes= new GameResponse("games", games);

        return listGamesRes;
    }

    public Object createGameService(String gameName, GameService service, Request req) throws DataAccessException {
        Integer gameID = service.createGame(gameName, req.headers("Authorization"));
        GameResponse createGameResponse = new GameResponse(gameID);

        System.out.println(String.format("in createGameService: %s",createGameResponse));
        return createGameResponse;
    }

    public Object joinGameService(GameService service, Request req) throws DataAccessException {
        HashSet<GameData> games = service.listGames(req.headers("Authorization"));
        GameResponse listGamesRes = new GameResponse("success", games);
        return listGamesRes;
    }

    public Class requestClass(){
        return GameData.class;
    }
}
