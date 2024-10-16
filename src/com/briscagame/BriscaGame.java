package com.briscagame;

import java.io.IOException;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ThreadPoolExecutor;

public class BriscaGame {

    public static void main(String[] args) throws IOException {

        // ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        // SimpleHttpServer.start(tpe);

        GameManager gameManager = new GameManager(new Game());
        gameManager.startSim();

        // Game game = new Game();
        // tpe.execute(game);
    }

}