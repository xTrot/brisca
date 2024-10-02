package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String response = "Hello, this is a simple HTTP server response!";
        HandlerHelper.sendResponse(exchange, 200, response);
    }
}
