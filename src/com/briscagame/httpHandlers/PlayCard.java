package com.briscagame.httpHandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EventListener;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.CardPlayedEvent;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class PlayCard implements HttpHandler {
    private static final int NO_RESPONSE = 0;
    
    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String json = HandlerHelper.post(exchange);
        if (json != null) {
            System.out.println(json);
            JSONObject parsedJson = new JSONObject(json);
            int index = parsedJson.getInt("index");
            CardPlayedEvent event = new CardPlayedEvent(this, index);
            this.notifyEvent(event);
            exchange.sendResponseHeaders(200, NO_RESPONSE);
            OutputStream os = exchange.getResponseBody();
            os.write("".getBytes());
            os.close();
        }
        
    }

    public void addListener(User user) {
        listeners.add(user);
    }

    private void notifyEvent(CardPlayedEvent event) {
        for(EventListener listener: listeners) {
            ((User)listener).doneThinking(event);
        }
    }
}
