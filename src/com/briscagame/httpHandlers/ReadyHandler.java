package com.briscagame.httpHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;

import javax.print.DocFlavor.STRING;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.CardPlayedEvent;
import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class ReadyHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int NOT_OK = 400;
    
    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        HandlerHelper.post(exchange);
        
        String userId = HandlerHelper.getSession(exchange).uuid;
        String gameId = HandlerHelper.getCookie(exchange, "gameId");
        if (this.notifyEvent(null, gameId, userId)) {
            HandlerHelper.sendStatus(exchange,OK);
            return;
        }
        
        HandlerHelper.sendStatus(exchange, NOT_OK);
        
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
