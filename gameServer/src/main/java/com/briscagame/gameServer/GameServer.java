package com.briscagame.gameServer;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class GameServer {
    static Game game;

    public static void main(String[] args) throws IOException {

        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Game.setTpe(tpe);
        SimpleHttpServer.start(tpe);

    }

    public static Game getGame() {
        return game;
    }

}