package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Lobby;
import com.sun.net.httpserver.HttpExchange;

public class LobbyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        HandlerHelper.getMethod(exchange);
        
        HandlerHelper.sendResponse(exchange, Status.OK, Lobby.getLobby());
        
    }

}
