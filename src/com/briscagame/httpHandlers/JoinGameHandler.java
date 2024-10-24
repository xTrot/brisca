package com.briscagame.httpHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.CardPlayedEvent;
import com.briscagame.Game;
import com.briscagame.Player;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class JoinGameHandler implements HttpHandler {
    private static final int OK = 200;
    
    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String json = HandlerHelper.post(exchange);

        if (json != null) {
            
            JSONObject parsedJson = new JSONObject(json);
            String gameId = parsedJson.getString("gameId");
            
            Session userSession = HandlerHelper.getSession(exchange);
            User user = new User(userSession);
            this.notifyEvent(null, gameId, user);
            HandlerHelper.sendStatus(exchange,OK);
        }
        
    }

    public void addListener(Game game) {
        listeners.add(game);
    }

    private void notifyEvent(CardPlayedEvent event, String gameId, Player user) {
        for(EventListener listener: listeners) {
            Game game = (Game) listener;
            if (gameId.equals(game.getUUID())) {
                game.addPlayer(user);
            }
        }
    }
}
