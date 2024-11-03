package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.sun.net.httpserver.HttpExchange;

public class LeaveGameHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        HandlerHelper.post(exchange);
        
        Session userSession = HandlerHelper.getSession(exchange);
        if (userSession == null) {
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

        if (game.removePlayer(userSession.getUserId())) {
            HandlerHelper.sendStatus(exchange, Status.OK);
            return;
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
    }

}
