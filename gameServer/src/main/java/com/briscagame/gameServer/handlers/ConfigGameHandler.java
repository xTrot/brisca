package com.briscagame.gameServer.handlers;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.briscagame.gameServer.Game;
import com.briscagame.gameServer.GamePostgresConnectionPool;
import com.briscagame.gameServer.GameServer;
import com.briscagame.gameServer.User;
import com.briscagame.httpHandlers.GameConfiguration;
import com.briscagame.httpHandlers.GameState;
import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Session;
import com.briscagame.httpHandlers.Status;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ConfigGameHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        String json = HandlerHelper.postMethod(exchange);

        if (json == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        JSONObject parsedJson = new JSONObject(json);
        if (parsedJson.isNull("maxPlayers")
                || parsedJson.isNull("swapBottomCard")
                || parsedJson.isNull("gameType")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String userId = HandlerHelper.getCookie(exchange, "userId");
        if (userId == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
        }

        Session userSession = Session.getSession(userId);
        if (userSession == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        if (!GamePostgresConnectionPool.sessionHasMylease(userId)) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        GameConfiguration gc;
        try {
            gc = new GameConfiguration(parsedJson);
        } catch (JSONException je) {
            System.err.println(je.getMessage());
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Game game = GameServer.getGame();

        if (game.getState().getState() == GameState.SPAWNED) {

            User user = new User(userSession);
            game.setGameConfiguration(gc);
            game.addPlayer(user);
            game.runGameThread();
            String gameId = game.getUUID();

            userSession.setGameID(gameId);
            userSession.setActionsSent(0);
            JSONObject gameJson = new JSONObject();
            gameJson.put("gameId", gameId);
            HandlerHelper.sendResponse(exchange, Status.OK, gameJson.toString());
            return;
        } else if (game.getState().getState() == GameState.WAITING_ROOM) {
            game.setGameConfiguration(gc);
            HandlerHelper.sendStatus(exchange, Status.OK);
            return;
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
    }

}
