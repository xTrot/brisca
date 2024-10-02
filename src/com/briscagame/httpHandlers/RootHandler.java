package com.briscagame.httpHandlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String response = "Hello, this is a simple HTTP server response!";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
