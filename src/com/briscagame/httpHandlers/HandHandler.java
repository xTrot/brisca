package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

public class HandHandler implements HttpHandler {

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

        String hand = user.getHand();
        if (hand == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }
        
        HandlerHelper.sendResponse(exchange, Status.OK, hand);
        
    }

}
