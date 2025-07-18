package sh.brisca.gameServer.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sh.brisca.common.HandlerHelper;
import sh.brisca.common.Session;
import sh.brisca.common.Status;
import sh.brisca.gameServer.Game;
import sh.brisca.gameServer.GameServer;

public class WaitingRoomHandler implements HttpHandler {

    // private static final Logger logger =
    // LoggerFactory.getLogger(WaitingRoomHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

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

        HandlerHelper.sendResponse(exchange, Status.OK, game.getWaitingRoom());

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);

    }

}
