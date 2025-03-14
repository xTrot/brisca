package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class JoinGameHandler implements HttpHandler {

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
        if (parsedJson.isNull("gameId")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String gameId = parsedJson.getString("gameId");

        Session userSession = HandlerHelper.getSession(exchange);
        if (userSession == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Game game = Game.getGame(gameId);
        if (game == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        if (!game.isJoinable()){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        User user = new User(userSession);
        if (game.addPlayer(user)){
            userSession.setGameID(gameId);
            userSession.setActionsSent(0);
            HandlerHelper.sendStatus(exchange, Status.OK);
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
    }

}
