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

public class SwapBottomCardHandler implements HttpHandler {

    // private static final Logger logger =
    // LoggerFactory.getLogger(SwapBottomCardHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HandlerHelper.postMethod(exchange);

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

        User user = game.getUser(userId);
        if (user == null) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        if (user.swapBottomCard()) {
            HandlerHelper.sendStatus(exchange, Status.OK);
        }

        HandlerHelper.sendStatus(exchange, Status.NOT_OK);
    }

}
