package com.briscagame;

import java.io.IOException;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONObject;

public class BriscaGame {

    public static void main(String[] args) throws IOException {

        // ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        // SimpleHttpServer.start(tpe);

        String[] configs = {
            "{\"maxPlayers\":4,\"swapBottomCard\":false}",
            "{\"maxPlayers\":4,\"swapBottomCard\":true}",
            "{\"maxPlayers\":3,\"swapBottomCard\":false}",
            "{\"maxPlayers\":3,\"swapBottomCard\":true}",
            "{\"maxPlayers\":2,\"swapBottomCard\":false}",
            "{\"maxPlayers\":2,\"swapBottomCard\":true}"
        };

        for (String config : configs) {
            GameConfiguration gc = new GameConfiguration(new JSONObject(config));
            System.out.println(gc);

            GameManager gameManager = new GameManager(new Game(), gc);
            gameManager.startSim();
            
        }

        // Game game = new Game();
        // tpe.execute(game);
    }

}