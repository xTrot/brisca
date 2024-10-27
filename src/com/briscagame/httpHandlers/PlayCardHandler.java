package com.briscagame.httpHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.CardPlayedEvent;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class PlayCardHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int NOT_OK = 200;
    
    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

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
        if (parsedJson.isNull("index")) {
            HandlerHelper.sendStatus(exchange, NOT_OK);
            return;
        }

        int index = parsedJson.optInt("index");
        Session userSession = HandlerHelper.getSession(exchange);
        CardPlayedEvent event = new CardPlayedEvent(this, index, userSession.uuid);
        this.notifyEvent(event);
        HandlerHelper.sendStatus(exchange,OK);
    }

    public void addListener(User user) {
        listeners.add(user);
    }

    private void notifyEvent(CardPlayedEvent event) {
        for(EventListener listener: listeners) {
            User user = (User)listener;
            if (event.getUuid().equals(user.getUuid())) {
                user.doneThinking(event);
            }
        }
    }
}
