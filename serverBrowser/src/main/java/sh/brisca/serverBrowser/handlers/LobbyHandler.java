package sh.brisca.serverBrowser.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sh.brisca.common.HandlerHelper;
import sh.brisca.common.Status;
import sh.brisca.serverBrowser.Lobby;

public class LobbyHandler implements HttpHandler {

    // private static final Logger logger =
    // LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HandlerHelper.sendResponse(exchange, Status.OK, Lobby.getLobby());

    }

}