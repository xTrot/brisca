package com.briscagame.serverBrowser.handlers;

import java.io.IOException;

import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Session;
import com.briscagame.httpHandlers.Status;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RefreshHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        System.out.println("Get Method");

        // String userId = "";
        String userId = HandlerHelper.getCookie(exchange, "userId");
        if (userId == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        System.out.println("Cookie userId: " + userId);

        Session userSession = Session.getSession(userId);
        if (userSession == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        System.out.println("Session exists.");

        if (HandlerHelper.refresh(exchange, userSession)) {
            HandlerHelper.sendStatus(exchange, Status.OK);
        }

        HandlerHelper.sendStatus(exchange, Status.OK);
    }

}
