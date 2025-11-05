package sh.brisca.gameServer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONObject;

public class WaitingRoom {

    public static final int TIMEOUT_DEFAULT = 2;

    // private static final Logger logger =
    // LoggerFactory.getLogger(WaitingRoom.class);

    private AtomicReference<String> cacheReference = new AtomicReference<String>(null);
    private Game game;
    Instant timeout;
    boolean timedOut = false;

    public WaitingRoom(Game game) {
        this.game = game;
    }

    public void updateWaitingRoom() {
        ArrayList<User> users = this.game.getPlayers();
        JSONObject json = new JSONObject();
        JSONArray playersJson = new JSONArray();
        for (User user : users) {

            String name = user.getPlayerName();
            boolean ready = user.isReady();
            String team = Player.TEAM_TYPES.get(user.getTeam());

            JSONObject playerJson = new JSONObject();
            playerJson.put("name", name);
            playerJson.put("ready", ready);
            playerJson.put("team", team);

            playersJson.put(playerJson);
        }

        String fill = this.game.getFillInfo();
        this.game.updateFill(fill);

        json.put("players", playersJson);
        json.put("fill", fill);
        json.put("started", game.hasStarted());
        json.put("type", this.game.getGameType());

        if (this.timedOut) {
            json.put("timedOut", this.timedOut);
        }

        cacheReference.set(json.toString());
    }

    public String getWaitingRoom() {
        return cacheReference.get();
    }

}
