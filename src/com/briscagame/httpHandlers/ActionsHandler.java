package com.briscagame.httpHandlers;

import java.io.IOException;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.sun.net.httpserver.HttpExchange;

public class ActionsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String json = HandlerHelper.get(exchange);

        if (json == null){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        JSONObject parsedJson = new JSONObject(json);
        if (parsedJson.isNull("from")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Session userSession = HandlerHelper.getSession(exchange);
        if (userSession ==  null){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String gameId = userSession.getGameID();
        if (gameId == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Game game = Game.getGame(gameId);
        if (game == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        int from = parsedJson.optInt("from");
        String actions = game.getActions(from);
        
        HandlerHelper.sendResponse(exchange, Status.OK, actions);
        
    }

}
