package com.briscagame.httpHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.CardPlayedEvent;
import com.briscagame.Game;
import com.briscagame.User;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class PlayCardHandler implements HttpHandler {
    
    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String json = HandlerHelper.post(exchange);

        if (json == null){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        JSONObject parsedJson = new JSONObject(json);
        if (parsedJson.isNull("index")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Session userSession = HandlerHelper.getSession(exchange);
        if (userSession == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String gameId = userSession.getGameID();
        if (gameId == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Game game = Game.getGame(gameId);
        if (game == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String userId = userSession.getUserId();
        User user = game.getUser(userId);
        if (user == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        int index = parsedJson.optInt("index");
        if (index < 0 || user.getHandSize() < index) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        CardPlayedEvent event = new CardPlayedEvent(this, index, userId);
        this.notifyEvent(event);
        HandlerHelper.sendStatus(exchange, Status.OK);
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
