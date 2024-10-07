package com.briscagame;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Game implements Runnable {

    private ArrayList<PlayAction> list = new ArrayList<PlayAction>();
    private AtomicReference<ArrayList<PlayAction>> listReference = new AtomicReference<ArrayList<PlayAction>>(list);

    public Game() {

        Game.registerConfigAction(this, new GameConfiguration());
        
    }

    @Override
    public void run() {
        GameManager gm = new GameManager();
        gm.startOnePlayer();
    }
    
    private static void registerConfigAction(Game game, GameConfiguration gameConfiguration) {
        String configString = gameConfiguration.toString();
        PlayAction action = new PlayAction(PlayAction.ActionType.GAME_CONFIGURATION);
        action.setPayload(configString);
        game.listReference.get().add(action);
    }

}
