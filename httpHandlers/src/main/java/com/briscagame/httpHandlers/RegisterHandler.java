package com.briscagame.httpHandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        String json = HandlerHelper.postMethod(exchange);

        if (json == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        JSONObject parsedJson = new JSONObject(json);
        if (parsedJson.isNull("username")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String username = parsedJson.getString("username");
        String userId = HandlerHelper.getCookie(exchange, "userId");
        if (username.length() < 3) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Session userSession = null;
        if (userId == null) {
            userSession = new Session(username);
            HandlerHelper.setCookie(exchange, "userId", userSession.getUserId());
            HandlerHelper.sendStatus(exchange, Status.OK);
            return;
        }

        userSession = Session.getSession(userId);
        if (userSession == null) {
            userSession = new Session(username);
            System.out.println("Refreshing session for " + username);
            HandlerHelper.setCookie(exchange, "userId", userSession.getUserId());
            HandlerHelper.sendStatus(exchange, Status.OK);
            return;
        }

        userSession.setUsername(username);
        HandlerHelper.sendStatus(exchange, Status.OK);
        System.out.println("Registered user: " + username + " with userId: " + userId);

    }

}
