package com.briscagame.gameServer.handlers;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.gameServer.GameServer;
import com.briscagame.gameServer.Game;
import com.briscagame.gameServer.Player;
import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Session;
import com.briscagame.httpHandlers.Status;
import com.sun.net.httpserver.HttpExchange;

public class ChangeTeamHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        String newTeam = null;
        String json = HandlerHelper.postMethod(exchange);

        JSONObject parsedJson = null;
        try {
            parsedJson = new JSONObject(json);
        } catch (JSONException e) {
        }

        if (parsedJson != null && parsedJson.has("team")) {
            String teamString = parsedJson.optString("team");
            if (Player.TEAM_TYPES.contains(teamString)) {
                newTeam = teamString;
            }
        }

        String userId = HandlerHelper.getCookie(exchange, "userId");
        if (userId == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Session userSession = Session.getSession(userId);
        if (userSession == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String gameId = userSession.getGameID();
        if (gameId == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Game game = GameServer.getGame();
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
