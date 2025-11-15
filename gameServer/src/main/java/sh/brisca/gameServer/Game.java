package sh.brisca.gameServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.brisca.common.GameConfiguration;
import sh.brisca.common.GameServerState;
import sh.brisca.common.GameState;
import sh.brisca.common.Session;
import sh.brisca.common.Stateful;

public class Game implements Runnable, EventListener, Stateful {

    private static final int HOST = 0;
    public static final String RECORDING_EXTENSION = ".js";
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    public static final Path RECORDING_DIR = Path.of(CURRENT_DIR, "recordings");

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

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
    private GameServerState state = new GameServerState();

    public Game(String host, String port) {
        this.uuid = UUID.randomUUID().toString();
        games.put(this.uuid, this);
        // Waiting room must be initialized before User joins.
        this.waitingRoom = new WaitingRoom(this);
        this.state.setServer(host + ":" + port);
    }

    @Override
    public void run() {

        Lobby.update();
        state.setState(GameState.WAITING_ROOM);
        new PlayAction(this, PlayAction.ActionType.GAME_CONFIG, new JSONObject(gameConfiguration.toString()));
        this.gameManager = new GameManager(this, this.gameConfiguration);
        this.waitingRoom();
        this.cleanUp();

    }

    private void waitingRoom() {

        logger.info("Join game: {}", this.uuid);
        this.waitingRoom.timeout = Instant.now().plus(WaitingRoom.TIMEOUT_DEFAULT, ChronoUnit.MINUTES);

        try {
            while (true) {

                this.waitingRoom.updateWaitingRoom();
                TimeUnit.MILLISECONDS.sleep(100);

                if (Instant.now().isAfter(this.waitingRoom.timeout)) {

                    this.waitingRoom.timedOut = true;
                    this.waitingRoom.updateWaitingRoom();
                    logger.info("The game {}, timed out.", this.uuid);
                    break;

                }

                if (this.startGameLock) {

                    state.setState(GameState.IN_PROGRESS);
                    gameManager.start(this.players);
                    this.gameCompleted = true;
                    break;

                }

            }

        } catch (Exception e) {
            logger.error("{}", e);
        }

    }

    private void cleanUp() {

        state.setState(GameState.COMPLETED);

        if (this.waitingRoom.timedOut) {
            Instant waitForKick = Instant.now().plusSeconds(5);
            while (true) {
                if (Instant.now().isAfter(waitForKick)) {
                    break;
                }
            }
        }

        for (User user : this.players) {
            Session.getSession(user.getUuid()).setGameID(null);
        }

        if (this.gameCompleted) {
            int THE_START = 0;
            String actions = this.getActions(THE_START);
            JSONArray gameRecording = new JSONArray(actions);
            Path filename = Path.of(RECORDING_DIR.toString(), this.uuid + RECORDING_EXTENSION);
            try {
                Files.createDirectories(RECORDING_DIR);
            } catch (IOException e) {
                logger.error("An error occurred creating directory: {}", e);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename.toString()))) {
                writer.write(gameRecording.toString());
                logger.info("Game recorded: {}", filename);
            } catch (IOException e) {
                logger.error("An error occurred writing to the file: {}", e);
            }
        }

        games.remove(this.uuid);
        Lobby.update();
        logger.info("Cleaned game {}.", this.uuid);

    }

    public synchronized boolean addPlayer(User user) {
        if (this.startGameLock || this.waitingRoom.timedOut)
            return false;
        String userId = user.getUuid();
        logger.info("Adding player {}: {}", user.getPlayerName(), userId);
        int playersSize = this.players.size();
        if (playersSize == 0) {
            this.players.add(user);
            this.waitingRoom.updateWaitingRoom();
            return true;
        }
        for (User existingUser : this.players) {
            if (userId.equals((existingUser).getUuid())) {
                logger.info("Player already joined, not added.");
                return false;
            }
        }
        if (playersSize >= this.gameConfiguration.getMaxPlayers()) {
            user.setTeam("S");
        }
        this.players.add(user);
        this.waitingRoom.updateWaitingRoom();
        Lobby.update();
        return true;
    }

    public synchronized boolean removePlayer(String userId) {
        if (this.startGameLock)
            return false;
        for (User user : players) {
            if (userId.equals(user.getUuid())) {
                players.remove(user);
                this.waitingRoom.updateWaitingRoom();
                Lobby.update();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean readyPlayer(String userId) {
        logger.info("Wants to ready player {}", userId);
        if (this.startGameLock)
            return false;
        if (this.players.size() == 0) {
            return false;
        }
        for (User user : this.players) {
            if (userId.equals(user.getUuid())) {
                logger.info("Player found and readied.");
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

        if (Instant.now().isAfter(this.waitingRoom.timeout))
            return false;

        if (!(players.get(HOST)).getUuid().equals(userId))
            return false;

        if (!this.ready())
            return false;

        this.startGameLock = true;
        try {

            while (!this.gameStarted) {
                TimeUnit.MILLISECONDS.sleep(10);
            }

        } catch (Exception e) {
            logger.error("{}", e);
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
        if (this.gameConfiguration.getGameType().equals(
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
        if (this.gameConfiguration.getMaxPlayers() == 4) {
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
        return gameConfiguration.getMaxPlayers() == playingCount;
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
        fill[1] = this.gameConfiguration.getMaxPlayers();
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
        return gameConfiguration.getGameType().equals(
                GameConfiguration.GAME_TYPE_STRINGS.get(GameConfiguration.PUBLIC));
    }

    public boolean isJoinable() {
        return !gameConfiguration.getGameType().equals(
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

    public String getGameType() {
        return gameConfiguration.getGameType();
    }

    @Override
    public GameServerState getState() {
        return this.state;
    }

    public void updateFill(String fill) {
        this.state.setFill(fill);
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        gameConfiguration.setGameId(this.uuid);
        this.gameConfiguration = gameConfiguration;
        this.state.setGameConfiguration(gameConfiguration);

    }

}
