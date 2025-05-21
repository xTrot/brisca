package com.briscagame.serverBrowser.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Status;
import com.briscagame.serverBrowser.Lobby;
import com.sun.net.httpserver.HttpExchange;

public class LobbyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HandlerHelper.sendResponse(exchange, Status.OK, Lobby.getLobby());

    }

}