package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.briscagame.GameConfiguration;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class MakeGameHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int NOT_OK = 400;

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String json = HandlerHelper.post(exchange);

        if (json == null){
            HandlerHelper.sendStatus(exchange, NOT_OK);
            return;
        }

        JSONObject parsedJson = new JSONObject(json);
        if (
            parsedJson.isNull("maxPlayers")
            || parsedJson.isNull("swapBottomCard")
            || parsedJson.isNull("gameType")
        ) {
            HandlerHelper.sendStatus(exchange, NOT_OK);
            return;
        }
        

        Session userSession = HandlerHelper.getSession(exchange);
        if (userSession ==  null){
            HandlerHelper.sendStatus(exchange, NOT_OK);
            return;
        }

        GameConfiguration gc;
        try {
            gc = new GameConfiguration(parsedJson);
        } catch (JSONException je){
            System.err.println(je.getMessage());
            HandlerHelper.sendStatus(exchange, NOT_OK);
            return;
        }

        Game newGame = new Game(gc);
        newGame.startGame();
        String gameId = newGame.getUUID();

        User user = new User(userSession);
        newGame.addPlayer(user);

        HandlerHelper.setCookie(exchange, "gameId", gameId);
        HandlerHelper.sendStatus(exchange, OK);
    }

}