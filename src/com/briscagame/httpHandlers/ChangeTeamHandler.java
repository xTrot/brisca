package com.briscagame.httpHandlers;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.briscagame.Player;
import com.sun.net.httpserver.HttpExchange;

public class ChangeTeamHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        // handle the request
        String newTeam = null;
        String json = HandlerHelper.post(exchange);

        JSONObject parsedJson = null;
        try {
            parsedJson = new JSONObject(json);
        } catch (JSONException e) {}

        if (parsedJson != null && parsedJson.has("team")){
            String teamString = parsedJson.optString("team");
            if (Player.TEAM_TYPES.contains(teamString)) {
                newTeam = teamString;
            } 
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

        if (game.changeTeam(userSession.getUserId(), newTeam)) {
            HandlerHelper.sendStatus(exchange, Status.OK);
            return;
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
    }

}
