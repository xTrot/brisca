package com.briscagame.serverBrowser.handlers;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Status;
import com.briscagame.serverBrowser.Lobby;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class JoinPrivateGameHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HashMap<String, String> params = HandlerHelper.getParams(exchange);

        if (!params.containsKey("gameId")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            logger.error("No param gameId.");
            return;
        }

        String gameId = params.get("gameId");

        JSONObject game = Lobby.getPrivateGame(gameId);
        if (game == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            logger.error("gameId: {} not found.", gameId);
            return;
        }

        HandlerHelper.sendResponse(exchange, Status.OK, game.toString());
    }

}
