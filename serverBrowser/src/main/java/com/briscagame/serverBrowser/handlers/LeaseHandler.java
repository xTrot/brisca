package com.briscagame.serverBrowser.handlers;

import java.io.IOException;

import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Session;
import com.briscagame.httpHandlers.Status;
import com.briscagame.serverBrowser.MakeGameLease;
import com.briscagame.serverBrowser.ServerBrowser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LeaseHandler implements HttpHandler {

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

        MakeGameLease lease = ServerBrowser.leasingOffice.getLease(userId);
        if (lease == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        System.out.println("Made lease: " + lease);

        HandlerHelper.sendResponse(exchange, Status.OK, lease.toString());
    }

}
