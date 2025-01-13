package com.briscagame.httpHandlers;

import java.io.IOException;

import org.json.JSONObject;

import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SeatHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        HandlerHelper.get(exchange);

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

        User user = game.getUser(userSession.getUserId());
        if (user == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String seat = new JSONObject().put("seat", user.getSeat()).toString();
        if (seat == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }
        
        HandlerHelper.sendResponse(exchange, Status.OK, seat);
        
    }

}
