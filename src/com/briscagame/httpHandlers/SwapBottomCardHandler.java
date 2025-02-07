package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

public class SwapBottomCardHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {

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

        String userId = userSession.getUserId();
        User user = game.getUser(userId);
        if (user == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        if (user.swapBottomCard()) {
            HandlerHelper.sendStatus(exchange, Status.OK);
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
    }

}
