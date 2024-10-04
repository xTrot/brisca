package com.briscagame;

public class Game implements Runnable {

    public Game() {
        
    }

    @Override
    public void run() {
        GameManager gm = new GameManager();
        gm.startOnePlayer();
    }

}
