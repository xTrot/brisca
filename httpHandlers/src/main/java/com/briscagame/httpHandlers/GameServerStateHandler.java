package com.briscagame.httpHandlers;

import java.io.IOException;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GameServerStateHandler implements HttpHandler {

    private Stateful game;

    public GameServerStateHandler(Stateful game) {
        this.game = game;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        String state = new JSONObject(game.getState()).toString();
        if (state == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        HandlerHelper.sendResponse(exchange, Status.OK, state);

    }

}
