package com.briscagame;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.EventListener;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.briscagame.httpHandlers.Session;

public class Game implements Runnable, EventListener {

    private static final int HOST = 0;
    public static final String RECORDING_EXTENSION = ".js";
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    public static final Path RECORDING_DIR = Path.of(CURRENT_DIR, "recordings");

    private static ThreadPoolExecutor tpe;

    // Protect these with synchro
    private static Hashtable<String, Game> games = new Hashtable<String, Game>();
    private ArrayList<String> actions = new ArrayList<String>();
    private ArrayList<User> players = new ArrayList<User>();
    private boolean startGameLock = false;
    private boolean gameStarted = false;
    private boolean gameCompleted = false;

    private GameManager gameManager;
    private GameConfiguration gameConfiguration;
    private String uuid;
    private WaitingRoom waitingRoom;

    public Game(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
        this.uuid = gameConfiguration.gameId;
        Game.games.put(this.uuid, this);
        new PlayAction(this, PlayAction.ActionType.GAME_CONFIG, new JSONObject(gameConfiguration.toString()));
        this.waitingRoom = new WaitingRoom(this);

    }

    @Override
    public void run() {
        Lobby.updateLobby();
        this.gameManager = new GameManager(this, this.gameConfiguration);
        this.waitingRoom();
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
        this.gameCompleted = true;
    }

    private void cleanUp() {
        Game.games.remove(this.uuid);
        Lobby.updateLobby();
        for (User user : this.players) {
            Session.getSession(user.getUuid()).setGameID(null);
        }
        if (this.gameCompleted) {
            int THE_START = 0;
            String actions = this.getActions(THE_START);
            JSONArray gameRecording = new JSONArray(actions);
            Path filename = Path.of(RECORDING_DIR.toString(), this.uuid + RECORDING_EXTENSION);
            System.out.println(filename);
            try {
                Files.createDirectories(RECORDING_DIR);
            } catch (IOException e) {
                System.err.println("An error occurred creating directory: " + e.getMessage());
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename.toString()))) {
                writer.write(gameRecording.toString());
                System.out.println("Game recorded: " + filename);
            } catch (IOException e) {
                System.err.println("An error occurred writing to the file: " + e.getMessage());
            }
        }
    }

    public synchronized boolean addPlayer(User user) {
        if (this.startGameLock)
            return false;
        String userId = user.getUuid();
        System.out.println("Adding player " + user.getPlayerName() + ": " + userId);
        int playersSize = this.players.size();
        if (playersSize == 0) {
            this.players.add(user);
            this.waitingRoom.updateWaitingRoom();
            return true;
        }
        for (User existingUser : this.players) {
            if (userId.equals((existingUser).getUuid())) {
                System.out.println("Player already joined, not added.");
                return false;
            }
        }
        if (playersSize >= this.gameConfiguration.maxPlayers) {
            user.setTeam("S");
        }
        this.players.add(user);
        Lobby.updateLobby();
        this.waitingRoom.updateWaitingRoom();
        return true;
    }

    public synchronized boolean removePlayer(String userId) {
        if (this.startGameLock)
            return false;
        for (User user : players) {
            if (userId.equals(user.getUuid())) {
                players.remove(user);
                Lobby.updateLobby();
                this.waitingRoom.updateWaitingRoom();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean readyPlayer(String userId) {
        System.out.println("Wants to ready player " + userId);
        if (this.startGameLock)
            return false;
        if (this.players.size() == 0) {
            return false;
        }
        for (User user : this.players) {
            if (userId.equals(user.getUuid())) {
                System.out.println("Player found and readied.");
                user.readyToggle();
                this.waitingRoom.updateWaitingRoom();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean changeTeam(String userId, String team) {
        if (this.startGameLock)
            return false;
        User user = this.getUser(userId);
        if (user != null) {
            boolean rtn = user.setTeam(team);
            this.waitingRoom.updateWaitingRoom();
            return rtn;
        }
        return false;
    }

    public synchronized boolean startGame(String userId) {
        if (this.startGameLock)
            return false;
        if (!(players.get(HOST)).getUuid().equals(userId)) {
            return false;
        }
        if (!this.ready()) {
            return false;
        }
        this.startGameLock = true;
        try {
            while (!this.gameStarted) {
                TimeUnit.MILLISECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
            this.cleanUp();
        }
        this.waitingRoom.updateWaitingRoom();
        return true;
    }

    public String getActions(Session userSession) {
        int actionsSize = actions.size();
        int from = userSession.getActionsSent();
        if (actionsSize == from)
            return "[]";
        StringBuilder actionsJsonArray = new StringBuilder("[");
        for (int i = from; i < actionsSize; i++) {
            actionsJsonArray.append(this.actions.get(i));
        }
        actionsJsonArray.replace(actionsJsonArray.length() - 1, actionsJsonArray.length(), "]");
        userSession.setActionsSent(actionsSize);
        return actionsJsonArray.toString();
    }

    public String getActions(int from) {
        int actionsSize = actions.size();
        if (actionsSize == from)
            return "[]";
        StringBuilder actionsJsonArray = new StringBuilder("[");
        for (int i = from; i < actionsSize; i++) {
            actionsJsonArray.append(this.actions.get(i));
        }
        actionsJsonArray.replace(actionsJsonArray.length() - 1, actionsJsonArray.length(), "]");
        return actionsJsonArray.toString();
    }

    public void runGameThread() {
        Game.tpe.execute(this);
    }

    private boolean ready() {
        if (this.gameConfiguration.gameType.equals(
                GameConfiguration.GAME_TYPE_STRINGS.get(GameConfiguration.SOLO))) {
            return this.players.get(HOST).isReady();
        }
        int playingCount = 0;
        for (Player player : players) {
            if (Player.TEAM_TYPES.get(player.getTeam()).equals("S"))
                continue;
            if (!player.isReady())
                return false;
            playingCount++;
        }
        if (this.gameConfiguration.maxPlayers == 4) {
            int teamB = 0;
            int teamA = 0;
            for (Player player : players) {
                int teamIndex = player.getTeam();
                switch (teamIndex) {
                    case 0:
                        teamB += 1;
                        break;
                    case 1:
                        teamA += 1;
                        break;
                    default:
                        break;
                }
            }
            return (teamB == 2 && teamA == 2);
        }
        return gameConfiguration.maxPlayers == playingCount;
    }

    public static void registerAction(Game game, PlayAction action) {
        game.actions.add(action.toString() + ","); // Storing with comma, ready for array construction.
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
        int[] fill = { 0, 0, 0 };
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
        return gameConfiguration.gameType.equals(
                GameConfiguration.GAME_TYPE_STRINGS.get(GameConfiguration.PUBLIC));
    }

    public boolean isJoinable() {
        return !gameConfiguration.gameType.equals(
                GameConfiguration.GAME_TYPE_STRINGS.get(GameConfiguration.SOLO));
    }

    public boolean hasStarted() {
        return this.startGameLock;
    }

    public User getUser(String userId) {
        User user = null;
        for (User existingUser : this.players) {
            if (userId.equals((existingUser).getUuid())) {
                user = existingUser;
            }
        }
        return user;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public String getWaitingRoom() {
        return waitingRoom.getWaitingRoom();
    }

    public void setGameStarted() {
        this.gameStarted = true;
    }

}
