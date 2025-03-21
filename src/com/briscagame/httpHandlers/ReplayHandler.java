package com.briscagame.httpHandlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import com.sun.net.httpserver.HttpHandler;
import com.briscagame.Game;
import com.sun.net.httpserver.HttpExchange;

public class ReplayHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        HandlerHelper.getMethod(exchange);
        HashMap<String, String> params = HandlerHelper.getParams(exchange);

        if (!params.containsKey("gameId")) {
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        String gameId = params.get("gameId");

        File filename = new File(Path.of(Game.RECORDING_DIR.toString(),
                gameId + Game.RECORDING_EXTENSION).toString());
        
        if (!filename.exists()){
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String actions = "";
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                actions += line;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
            System.err.println(e);
            reader.close();
            HandlerHelper.sendStatus(exchange, Status.NOT_OK);
            return;
        }

        HandlerHelper.sendResponse(exchange, Status.OK, actions);
        reader.close();
    }

}
