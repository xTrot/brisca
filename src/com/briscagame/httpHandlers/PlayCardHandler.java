package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class PlayCardHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String json = HandlerHelper.postMethod(exchange);

        if (json == null){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        JSONObject parsedJson = new JSONObject(json);
        if (parsedJson.isNull("index")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

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

        int index = parsedJson.optInt("index");
        if (index < 0 || user.getHandSize() <= index) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        if (user.doneThinking(index)) {
            HandlerHelper.sendStatus(exchange, Status.OK);
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
    }

}
