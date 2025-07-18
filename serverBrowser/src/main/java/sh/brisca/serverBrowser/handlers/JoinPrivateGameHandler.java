package sh.brisca.serverBrowser.handlers;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sh.brisca.common.HandlerHelper;
import sh.brisca.common.Status;
import sh.brisca.serverBrowser.Lobby;

public class JoinPrivateGameHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HashMap<String, String> params = HandlerHelper.getParams(exchange);

        if (!params.containsKey("gameId")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            logger.error("No param gameId.");
            return;
        }

        String gameId = params.get("gameId");

        JSONObject game = Lobby.getPrivateGame(gameId);
        if (game == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            logger.error("gameId: {} not found.", gameId);
            return;
        }

        HandlerHelper.sendResponse(exchange, Status.OK, game.toString());
    }

}
