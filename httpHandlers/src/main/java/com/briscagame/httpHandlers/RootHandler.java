package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

    // private static Logger logger = LoggerFactory.getLogger(RootHandler.class);

    private String serverType;

    public RootHandler(String serverType) {
        this.serverType = serverType;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        String response = "Hello, I am a " + this.serverType + "!";
        HandlerHelper.sendResponse(exchange, Status.OK, response);
    }
}
