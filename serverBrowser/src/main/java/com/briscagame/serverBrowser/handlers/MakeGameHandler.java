package com.briscagame.serverBrowser.handlers;

import java.io.IOException;
import java.util.UUID;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.httpHandlers.HandlerHelper;
import com.briscagame.httpHandlers.Status;
import com.briscagame.serverBrowser.EnvironmentVariable;
import com.briscagame.serverBrowser.HealthCheck;
import com.briscagame.serverBrowser.Spawner;
import com.sun.net.httpserver.HttpExchange;

import org.json.*;

public class MakeGameHandler implements HttpHandler {
    private int lastSpawnedServer = 0;

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

        String gameId = UUID.randomUUID().toString();
        int port = (EnvironmentVariable.BROWSER_GAME_PORT_RANGE_START +
                lastSpawnedServer % EnvironmentVariable.BROWSER_GAME_PORT_RANGE_COUNT);
        String gameServer = Spawner.spawn(parsedJson, gameId, port);

        if (!HealthCheck.check(gameServer, "5", "1", "10")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        JSONObject gameJson = new JSONObject("{\"gameId\":\"" + gameId + "\"}");
        HandlerHelper.sendResponse(exchange, Status.OK, gameJson.toString());
    }

}
