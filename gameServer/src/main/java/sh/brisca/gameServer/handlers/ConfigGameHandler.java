package sh.brisca.gameServer.handlers;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sh.brisca.common.GameConfiguration;
import sh.brisca.common.GameState;
import sh.brisca.common.HandlerHelper;
import sh.brisca.common.Session;
import sh.brisca.common.Status;
import sh.brisca.gameServer.Game;
import sh.brisca.gameServer.SimpleHttpServer;
import sh.brisca.gameServer.User;

// TODO: Rename class to MakeGameHandler.
public class ConfigGameHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConfigGameHandler.class);

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
            logger.error("Error getting cookie userId for exchange: {}", exchange);
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Session userSession = Session.getSession(userId);
        if (userSession == null) {
            logger.info("Didn't find it in the db either.");
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        GameConfiguration gc;
        try {
            gc = new GameConfiguration(parsedJson);
        } catch (JSONException je) {
            logger.error("{}", je);
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        Game game = new Game(SimpleHttpServer.getHostname(), Integer.toString(SimpleHttpServer.getPort()));

        if (game.getState().getState() == GameState.SPAWNED) {

            User user = new User(userSession);
            game.setGameConfiguration(gc);
            game.addPlayer(user);
            game.runGameThread();
            String gameId = game.getUUID();

            userSession.setGameID(gameId);
            HandlerHelper.setCookie(exchange, "gameId", gameId,
                    Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS)));
            userSession.setActionsSent(0);
            JSONObject gameJson = new JSONObject();
            gameJson.put("gameId", gameId);
            gameJson.put("gameServer", game.getState().getServer());
            HandlerHelper.sendResponse(exchange, Status.OK, gameJson.toString());

            return;
        } else if (game.getState().getState() == GameState.WAITING_ROOM) {
            game.setGameConfiguration(gc);
            HandlerHelper.sendStatus(exchange, Status.OK);
            logger.info("Registering Game: {}", game.getUUID());

            return;
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);

    }

}
