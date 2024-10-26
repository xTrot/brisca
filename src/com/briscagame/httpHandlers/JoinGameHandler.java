package com.briscagame.httpHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedHashMap;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.CardPlayedEvent;
import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class JoinGameHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int NOT_OK = 400;
    
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
        if (parsedJson.isNull("gameId")) {
            HandlerHelper.sendStatus(exchange, NOT_OK);
            return;
        }

        String gameId = parsedJson.getString("gameId");
        LinkedHashMap<String,String> cookies = HandlerHelper.getCookies(exchange);

        Session userSession = null;
        if (!cookies.containsKey("userId")) {
            userSession = new Session();
            cookies.put("userId", userSession.uuid);
        }

        userSession = HandlerHelper.getSession(exchange);

        User user = new User(userSession);
        
        if (this.notifyEvent(null, gameId, user)){
            cookies.put("gameId", gameId);
            HandlerHelper.setCookies(exchange, cookies);
            HandlerHelper.sendStatus(exchange, OK);
        }
        
    }

    public void addListener(Game game) {
        listeners.add(game);
    }

    private boolean notifyEvent(CardPlayedEvent event, String gameId, User user) {
        for(EventListener listener: listeners) {
            Game game = (Game) listener;
            if (gameId.equals(game.getUUID())) {
                return game.addPlayer(user);
            }
        }
        return false;
    }
}
