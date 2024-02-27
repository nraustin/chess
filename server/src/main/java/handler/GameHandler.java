package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import request.JoinGameRequest;
import response.GameResponse;
import service.GameService;
import spark.Request;

import java.util.HashSet;

public class GameHandler extends BaseHandler{
    public GameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        super(userDAO, gameDAO, authDAO);
    }

    public Object performService(Object reqObject, Request req) throws DataAccessException {
        GameService service = new GameService(userDAO, gameDAO, authDAO);

        switch (req.requestMethod()){
            case "GET":
                return listGamesService(service, req);
            case "POST":
                return createGameService((GameData)reqObject, service, req);
            case "PUT":
                System.out.println(String.format("join game request Object: %s", reqObject));
                return joinGameService((JoinGameRequest)reqObject, service, req);
            default:
                throw new DataAccessException(400, "Error: unsupported request method");
        }
    }

    public Object listGamesService(GameService service, Request req) throws DataAccessException {
        HashSet<GameData> games = service.listGames(req.headers("Authorization"));
        GameResponse listGamesRes= new GameResponse("games", games);

        return listGamesRes;
    }

    public Object createGameService(GameData newGame, GameService service, Request req) throws DataAccessException {
        Integer gameID = service.createGame(newGame.gameName(), req.headers("Authorization"));
        GameResponse createGameResponse = new GameResponse(gameID);

        System.out.println(String.format("in createGameService: %s",createGameResponse));
        return createGameResponse;
    }

    public Object joinGameService(JoinGameRequest joinGameReq, GameService service, Request req) throws DataAccessException {
        service.joinGame(joinGameReq.getGameID(), joinGameReq.getPlayerColor(), req.headers("Authorization"));
        return null;
    }

    public Class requestClass(Request req){
        if(req.requestMethod() == "PUT"){
            return JoinGameRequest.class;
        }
        return GameData.class;
    }
}
