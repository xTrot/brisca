package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class RegisterHandler implements HttpHandler {
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
        if (parsedJson.isNull("username")) {
            HandlerHelper.sendStatus(exchange, NOT_OK);
            return;
        }

        String username = parsedJson.getString("username");
        String userId = HandlerHelper.getCookie(exchange, "userId");

        Session userSession = null;
        if (userId == null) {
            userSession = new Session(username);
            HandlerHelper.setCookie(exchange, "userId", userSession.uuid);
            HandlerHelper.sendStatus(exchange, OK);
            return;
        }

        userSession = HandlerHelper.getSession(exchange);
        userSession.username = username;
        HandlerHelper.sendStatus(exchange, OK);
        
    }

}
