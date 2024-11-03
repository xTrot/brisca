package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.sun.net.httpserver.HttpExchange;

public class StartGameHandler implements HttpHandler {

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

        String userId = userSession.getUserId();
        String gameId = userSession.getGameID();
        Game game = Game.getGame(gameId);
        if (game.startGame(userId)) {
            HandlerHelper.sendStatus(exchange,Status.OK);
            return;
        }
        
        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
        
    }

}
