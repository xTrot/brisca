package com.briscagame.httpHandlers;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RegisterHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

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
            HandlerHelper.setCookie(exchange, "userId", userSession.getUserId(), userSession.getRefreshBy());
            HandlerHelper.sendStatus(exchange, Status.OK);
            return;
        }

        userSession = Session.getSession(userId);
        if (userSession == null) {
            userSession = new Session(username);
            logger.info("Refreshing session for {}", username);
            HandlerHelper.setCookie(exchange, "userId", userSession.getUserId(), userSession.getRefreshBy());
            HandlerHelper.sendStatus(exchange, Status.OK);
            return;
        }

        userSession.setUsername(username);
        HandlerHelper.sendStatus(exchange, Status.OK);
        logger.info("Registered user: {} with userId: {}", username, userId);

    }

}
