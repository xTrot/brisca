package com.briscagame.serverBrowser;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONArray;
import org.json.JSONObject;

public class Lobby {
    private static AtomicReference<String> cacheReference = new AtomicReference<String>("[]");

    private static Hashtable<String, GameServerInfo> games = new Hashtable<String, GameServerInfo>();

    public static void updateLobby() {
        JSONObject json = new JSONObject();
        JSONArray gamesJson = new JSONArray();
        for (String key : games.keySet()) {
            GameServerInfo game = games.get(key);
            if (!game.isPublic() || game.hasStarted()) {
                continue;
            }
            String fill = game.getFillInfo();
            JSONObject gameJSON = new JSONObject();
            gameJSON.put("gameId", key);
            gameJSON.put("fill", fill);
            gamesJson.put(gameJSON);
        }
        json.put("games", gamesJson);
        cacheReference.set(json.toString());
    }

    public static String getLobby() {
        return cacheReference.get();
    }

}
