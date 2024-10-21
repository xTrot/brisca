package com.briscagame;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;

public class Game implements Runnable {

    private ArrayList<PlayAction> list = new ArrayList<PlayAction>();
    private AtomicReference<ArrayList<PlayAction>> listReference = new AtomicReference<ArrayList<PlayAction>>(list);
    private GameManager gameManager;
    private GameConfiguration gameConfiguration;

    public Game(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
        Game.registerConfigAction(this, this.gameConfiguration);
        
    }

    @Override
    public void run() {
        this.gameManager = new GameManager(this, this.gameConfiguration);
        if(gameConfiguration.gameType.equals("public")) this.waitingRoom();
        gameManager.startOnePlayer();
    }
    
    private void waitingRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'waitingRoom'");
    }

    private static void registerConfigAction(Game game, GameConfiguration gameConfiguration) {
        PlayAction action = new PlayAction(PlayAction.ActionType.GAME_SETUP_COMPLETED, new JSONObject(gameConfiguration));
        game.listReference.get().add(action);
    }

    public static void registerAction(Game game, PlayAction action) {
        game.listReference.get().add(action);
    }

}
