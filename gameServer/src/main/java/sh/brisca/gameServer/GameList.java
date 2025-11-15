package sh.brisca.gameServer;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONArray;
import org.json.JSONObject;

public class GameList {

    // private static final Logger logger = LoggerFactory.getLogger(GameList.class);

    private static AtomicReference<String> cacheReference = new AtomicReference<String>("{games:[]}");

    public static void update() {

        Hashtable<String, Game> games = Game.getGames();
        JSONObject json = new JSONObject();
        JSONArray gamesJson = new JSONArray();

        for (String key : games.keySet()) {

            Game game = games.get(key);
            JSONObject gameStateJSON = new JSONObject(game.getState());
            gamesJson.put(gameStateJSON);

        }

        json.put("games", gamesJson);
        cacheReference.set(json.toString());

    }

    public static String get() {
        return cacheReference.get();
    }

}
