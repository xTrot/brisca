package sh.brisca.gameServer.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sh.brisca.common.HandlerHelper;
import sh.brisca.common.Session;
import sh.brisca.common.Status;
import sh.brisca.gameServer.Game;
import sh.brisca.gameServer.GameServer;
import sh.brisca.gameServer.User;

public class HandHandler implements HttpHandler {

    // private static final Logger logger =
    // LoggerFactory.getLogger(HandHandler.class);

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

        User user = game.getUser(userSession.getUserId());
        if (user == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String hand = user.getHand();
        if (hand == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        HandlerHelper.sendResponse(exchange, Status.OK, hand);

    }

}
