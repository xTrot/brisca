package com.briscagame;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;
import java.util.EventListener;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.briscagame.httpHandlers.Session;

public class Game implements Runnable, EventListener {
    
    private static final int HOST = 0;

    private static ThreadPoolExecutor tpe;

    // Protect these with synchro
    private static Hashtable<String,Game> games = new Hashtable<String,Game>();
    private ArrayList<PlayAction> actions = new ArrayList<PlayAction>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private boolean startGameLock = false;

    private GameManager gameManager;
    private GameConfiguration gameConfiguration;
    private String uuid = UUID.randomUUID().toString();

    public Game(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
        Game.games.put(this.uuid,this);
        Game.registerConfigAction(this, this.gameConfiguration);
        
    }

    @Override
    public void run() {
        Lobby.updateLobby();
        this.gameManager = new GameManager(this, this.gameConfiguration);
        if (gameConfiguration.gameType.equals("public")) {
            this.waitingRoom();
        } else {
            gameManager.startOnePlayer();
        }
        this.cleanUp();
    }
    
    private void waitingRoom() {
        // Players should be able to join, leave an change teams. Done
        // While players not ready or 2 minutes veryone gets kicked.
        System.out.println("Join game: " + this.uuid);
        try {
            while (!this.startGameLock) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
            this.cleanUp();
        }

        gameManager.start(this.players);
    }

    private void cleanUp() {
        Game.games.remove(this.uuid);
        Lobby.updateLobby();
        for (Player player : this.players) {
            Session.getSession(((User)player).getUuid()).setGameID(null);;
        }
    }

    public synchronized boolean addPlayer(User user) {
        if (this.startGameLock) return false;
        String userId = user.getUuid();
        System.out.println("Adding player " + user.getPlayerName() + ":" + userId);
        if (this.players.size() == 0){
            this.players.add(user);
            return true;
        }
        for (Player player : this.players) {
            if (userId.equals(((User)player).getUuid())) {
                System.out.println("Player already joined, not added.");
                return false;
            }
        }
        this.players.add(user);
        return true;
    }

    public synchronized boolean removePlayer(String userId) {
        if (this.startGameLock) return false;
        for (Player player : players) {
            if(userId.equals(((User)player).getUuid())){
                players.remove(player);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean readyPlayer(String userId) {
        System.out.println("Wants to ready player " + userId);
        if (this.startGameLock) return false;
        if (this.players.size() == 0){
            return false;
        }
        for (Player player : this.players) {
            if (userId.equals(((User)player).getUuid())) {
                System.out.println("Player found and readied.");
                player.readyToggle();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean changeTeam(String userId, String team) {
        if (this.startGameLock) return false;
        if (this.players.size() == 0) return false;
        for (Player player : this.players) {
            return player.setTeam(team);
        }
        return false;
    }

    public synchronized boolean startGame(String userId) {
        if (this.startGameLock) return false;
        if (!((User)players.get(HOST)).getUuid().equals(userId)){
            return false;
        }
        if (!this.ready(players)){
            return false;
        }
        this.startGameLock = true;
        return true;
    }

    public String getActions(int from) {
        JSONArray actionsJson = new JSONArray();
        for (int i = from; i < this.actions.size(); i++) {
            actionsJson.put(this.actions.get(i));
        }
        return actionsJson.toString();
    }

    public void runGameThread() {
        Game.tpe.execute(this);
    }

    private boolean ready(ArrayList<Player> players) {
        if (players.size() != gameConfiguration.maxPlayers) return false;
        for (Player player : players) {
            if (!player.isReady()) return false;
        }
        if (this.gameConfiguration.maxPlayers == 4) {
            int team1 = 0;
            int teamA = 0;
            for (Player player : players) {
                int teamIndex = player.getTeam();
                switch (teamIndex) {
                    case 0:
                        team1 +=1;
                        break;
                    case 1:
                        teamA +=1;
                        break;
                    default:
                        break;
                }
            }
            return (team1==2 && teamA==2);
        }
        return true;
    }

    private static void registerConfigAction(Game game, GameConfiguration gameConfiguration) {
        PlayAction action = new PlayAction(PlayAction.ActionType.GAME_SETUP_COMPLETED, new JSONObject(gameConfiguration));
        game.actions.add(action);
    }

    public static void registerAction(Game game, PlayAction action) {
        game.actions.add(action);
    }

    public static Game getGame(String gameId) {
        return Game.games.get(gameId);
    }

    public static Hashtable<String, Game> getGames() {
        return Game.games;
    }

    public String getUUID() {
        return this.uuid;
    }

    public static void setTpe(ThreadPoolExecutor tpe) {
        Game.tpe = tpe;
    }

    public String getFillInfo() {
        int[] fill = {0, 0, 0};
        for (Player player : this.players) {
            int team = player.getTeam();
            switch (team) {
                case 0:
                    fill[0] += 1;
                    break;
                case 1:
                    fill[0] += 1;
                    break;
            
                case 2:
                    fill[2] += 1;
                    break;
            
                default:
                    break;
            }
        }
        fill[1] = this.gameConfiguration.maxPlayers;
        String fillString;
        StringBuilder sb = new StringBuilder();
        sb.append(fill[0]);
        sb.append("/");
        sb.append(fill[1]);
        sb.append("/");
        sb.append(fill[2]);
        fillString = sb.toString();
        return fillString;
    }

    public boolean isPublic() {
        return gameConfiguration.gameType.equals("public");
    }

    public User getUser(String userId) {
        User user = null;
        for (Player player : this.players) {
            if (userId.equals(((User)player).getUuid())) {
                user = (User)player;
            }
        }
        return user;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

}
