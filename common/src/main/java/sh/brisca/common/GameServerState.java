package sh.brisca.common;

import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

public class GameServerState {

    // private static final Logger logger =
    // LoggerFactory.getLogger(GameServerState.class);

    private GameState state = GameState.SPAWNED;
    private GameConfiguration gameConfiguration = null;
    private String fill = null;
    private String server = null;

    public GameServerState() {
    }

    public GameServerState(JSONObject json) {
        GameState state = json.getEnum(GameState.class, "state");
        JSONObject gcJson = json.optJSONObject("gameConfiguration");
        GameConfiguration gc = null;
        if (gcJson != null) {
            gc = new GameConfiguration(gcJson);
        }
        String fill = json.optString("fill");
        String server = json.optString("server");
        this.state = state;
        this.gameConfiguration = gc;
        this.fill = fill;
        this.server = server;

    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getFill() {
        return fill;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @JSONPropertyIgnore
    public boolean isPublic() {
        if (gameConfiguration == null) {
            return false;
        }
        return gameConfiguration.getGameType()
                .equals(GameConfiguration.GAME_TYPE_STRINGS
                        .get(GameConfiguration.PUBLIC));
    }

    @JSONPropertyIgnore
    public boolean hasStarted() {
        return state.equals(GameState.IN_PROGRESS)
                || state.equals(GameState.COMPLETED);
    }

    @Override
    public String toString() {
        return (new JSONObject(this)).toString();
    }
}
