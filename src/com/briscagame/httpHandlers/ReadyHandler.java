package com.briscagame.httpHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedHashMap;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.CardPlayedEvent;
import com.briscagame.Game;
import com.sun.net.httpserver.HttpExchange;

public class ReadyHandler implements HttpHandler {
    
    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        HandlerHelper.post(exchange);
        
        String userId = HandlerHelper.getSession(exchange).uuid;
        LinkedHashMap<String, String> cookies = HandlerHelper.getCookies(exchange);

        if (!cookies.containsKey("userId")) {
            System.out.println("Trying to ready w/o userId.");
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
        }

        if (!cookies.containsKey("gameId")) {
            System.out.println("Trying to ready w/o gameId.");
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
        }

        String gameId = cookies.get("gameId");
        if (this.notifyEvent(null, gameId, userId)) {
            HandlerHelper.sendStatus(exchange,Status.OK);
            return;
        }
        
        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
        
    }

    public void addListener(Game game) {
        listeners.add(game);
    }

    private boolean notifyEvent(CardPlayedEvent event, String gameId, String userId) {
        for(EventListener listener: listeners) {
            Game game = (Game) listener;
            if (gameId.equals(game.getUUID())) {
                return game.readyPlayer(userId);
            }
        }
        return false;
    }
}
