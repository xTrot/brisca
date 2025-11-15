package sh.brisca.gameServer;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONArray;
import org.json.JSONObject;

public class Lobby {

    private static AtomicReference<String> cacheReference = new AtomicReference<String>("[]");

    public static void update() {

        GameList.update();

        Hashtable<String, Game> games = Game.getGames();
        JSONObject json = new JSONObject();
        JSONArray gamesJson = new JSONArray();

        for (String key : games.keySet()) {

            Game game = games.get(key);
            if (!game.isPublic() || game.hasStarted()) {
                continue;
            }

            String fill = game.getFillInfo();
            JSONObject gameJSON = new JSONObject();
            gameJSON.put("gameId", key);
            gameJSON.put("fill", fill);
            gameJSON.put("server", game.getState().getServer());
            gamesJson.put(gameJSON);

        }

        json.put("games", gamesJson);
        cacheReference.set(json.toString());

    }

    public static String get() {
        return cacheReference.get();
    }

}
