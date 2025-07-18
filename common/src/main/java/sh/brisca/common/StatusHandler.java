package sh.brisca.common;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StatusHandler implements HttpHandler {

    // private static Logger logger = LoggerFactory.getLogger(StatusHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);

        HandlerHelper.sendStatus(exchange, Status.OK);

    }

}
