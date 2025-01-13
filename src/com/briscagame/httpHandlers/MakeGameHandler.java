package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.briscagame.GameConfiguration;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class MakeGameHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String json = HandlerHelper.post(exchange);

        if (json == null){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        JSONObject parsedJson = new JSONObject(json);
        if (
            parsedJson.isNull("maxPlayers")
            || parsedJson.isNull("swapBottomCard")
            || parsedJson.isNull("gameType")
        ) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }
        

        Session userSession = HandlerHelper.getSession(exchange);
        if (userSession ==  null){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        GameConfiguration gc;
        try {
            gc = new GameConfiguration(parsedJson);
        } catch (JSONException je){
            System.err.println(je.getMessage());
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        User user = new User(userSession);

        Game newGame = new Game(gc);
        newGame.addPlayer(user);
        newGame.runGameThread();
        String gameId = newGame.getUUID();

        userSession.setGameID(gameId);
        userSession.setActionsSent(0);
        JSONObject gameJson = new JSONObject("{\"gameId\":\""+gameId+"\"}");
        HandlerHelper.sendResponse(exchange, Status.OK, gameJson.toString());
    }

}
