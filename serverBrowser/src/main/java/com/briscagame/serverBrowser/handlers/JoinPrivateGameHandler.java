package com.briscagame.serverBrowser.handlers;

import java.io.IOException;
import java.util.HashMap;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Status;
import com.briscagame.serverBrowser.Lobby;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class JoinPrivateGameHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HashMap<String, String> params = HandlerHelper.getParams(exchange);

        if (!params.containsKey("gameId")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            System.err.println("No param gameId.");
            return;
        }

        String gameId = params.get("gameId");

        JSONObject game = Lobby.getPrivateGame(gameId);
        if (game == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            System.err.println("gameId:" + gameId + " not found.");
            return;
        }

        HandlerHelper.sendResponse(exchange, Status.OK, game.toString());
    }

}
