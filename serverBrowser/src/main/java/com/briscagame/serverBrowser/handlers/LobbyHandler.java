package com.briscagame.serverBrowser.handlers;

import java.io.IOException;

import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Status;
import com.briscagame.serverBrowser.Lobby;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LobbyHandler implements HttpHandler {

    // private static final Logger logger =
    // LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HandlerHelper.sendResponse(exchange, Status.OK, Lobby.getLobby());

    }

}