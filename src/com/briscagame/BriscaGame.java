package com.briscagame;

import java.io.IOException;

public class BriscaGame {

    public static void main(String[] args) throws IOException {

        
        SimpleHttpServer.start();

        GameManager gameManager = new GameManager();
        gameManager.startSim();

        gameManager = new GameManager();
        gameManager.startOnePlayer();
    }

}