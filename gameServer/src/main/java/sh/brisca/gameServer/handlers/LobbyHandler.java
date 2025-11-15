package sh.brisca.gameServer.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sh.brisca.common.HandlerHelper;
import sh.brisca.common.Status;
import sh.brisca.gameServer.Lobby;

public class LobbyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HandlerHelper.sendResponse(exchange, Status.OK, Lobby.get());

    }

}
