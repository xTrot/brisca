package com.briscagame;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;
import java.util.EventListener;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;

public class Game implements Runnable, EventListener {
    
    private static Hashtable<String,Game> games = new Hashtable<String,Game>();
    private static AtomicReference<Hashtable<String,Game>> gamesReference = new AtomicReference<Hashtable<String,Game>>(games);

    private ArrayList<PlayAction> actions = new ArrayList<PlayAction>();
    private AtomicReference<ArrayList<PlayAction>> listReference = new AtomicReference<ArrayList<PlayAction>>(actions);
    private GameManager gameManager;
    private GameConfiguration gameConfiguration;
    private ArrayList<Player> players = new ArrayList<Player>();
    private String uuid = UUID.randomUUID().toString();

    public Game(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
        Game.gamesReference.get().put(uuid,this);
        Game.registerConfigAction(this, this.gameConfiguration);
        SimpleHttpServer.getJoinGameHandler().addListener(this);
        
    }

    @Override
    public void run() {
        this.gameManager = new GameManager(this, this.gameConfiguration);
        if (gameConfiguration.gameType.equals("public")) {
            this.waitingRoom();
        } else {
            gameManager.startOnePlayer();
        }
    }
    
    private void waitingRoom() {
        // Players should be able to join, leave an change teams.
        // While players not ready or 2 minutes veryone gets kicked.
        System.out.println("Join game: " + uuid);
        while (!ready(players)) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cleanUp();
            }
        }
        gameManager.start(players);
    }

    private void cleanUp() {
        Game.gamesReference.get().remove(this.uuid);
    }

    public boolean addPlayer(User user) {
        String uuid = user.getUuid();
        System.out.println("Adding player " + user.getPlayerName() + ":" + uuid);
        if (players.size() ==0){
            this.players.add(user);
            return true;
        }
        for (Player player : this.players) {
            if (uuid.equals(((User)player).getUuid())) {
                System.out.println("Player already joined, not added.");
                return false;
            }
        }
        this.players.add(user);
        return true;
    }

    private boolean ready(ArrayList<Player> players) {
        if (players.size() != gameConfiguration.maxPlayers) return false;
        for (Player player : players) {
            if (!player.isReady()) return false;
        }
        return true;
    }

    private static void registerConfigAction(Game game, GameConfiguration gameConfiguration) {
        PlayAction action = new PlayAction(PlayAction.ActionType.GAME_SETUP_COMPLETED, new JSONObject(gameConfiguration));
        game.listReference.get().add(action);
    }

    public static void registerAction(Game game, PlayAction action) {
        game.listReference.get().add(action);
    }

    public static Hashtable<String, Game> getGames() {
        return Game.gamesReference.get();
    }

    public String getUUID() {
        return this.uuid;
    }

}
