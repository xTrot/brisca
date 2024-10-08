package com.briscagame;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;

public class Game implements Runnable {

    private ArrayList<PlayAction> list = new ArrayList<PlayAction>();
    private AtomicReference<ArrayList<PlayAction>> listReference = new AtomicReference<ArrayList<PlayAction>>(list);

    public Game() {

        Game.registerConfigAction(this, new GameConfiguration());
        
    }

    @Override
    public void run() {
        GameManager gm = new GameManager(this);
        gm.startOnePlayer();
    }
    
    private static void registerConfigAction(Game game, GameConfiguration gameConfiguration) {
        PlayAction action = new PlayAction(PlayAction.ActionType.GAME_SETUP_COMPLETED, new JSONObject(gameConfiguration));
        game.listReference.get().add(action);
    }

    public static void registerAction(Game game, PlayAction action) {
        game.listReference.get().add(action);
    }

}
