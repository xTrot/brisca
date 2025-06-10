package com.briscagame.serverBrowser;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONArray;
import org.json.JSONObject;

import com.briscagame.httpHandlers.GameConfiguration;
import com.briscagame.httpHandlers.GameServerState;

public class Lobby {
    private static AtomicReference<String> cacheReference = new AtomicReference<String>("[]");
    private static LinkedHashMap<String, JSONObject> privateGames = new LinkedHashMap<String, JSONObject>();

    public static void updateLobby() {
        LinkedHashMap<String, JSONObject> updatePrivateGames = new LinkedHashMap<String, JSONObject>();
        JSONObject json = new JSONObject();
        JSONArray gamesJson = new JSONArray();
        for (GameServerState state : GameServerPool.gameServers) {
            GameConfiguration gc = state != null ? state.getGameConfiguration() : null;
            if (state == null || gc == null || state.hasStarted()) {
                continue;
            }
            String fill = state.getFill();
            JSONObject gameJSON = new JSONObject();
            String gameId = state.getGameConfiguration().gameId;
            gameJSON.put("gameId", gameId);
            gameJSON.put("fill", fill);
            gameJSON.put("server", state.getServer());
            if (state.isPublic()) {
                gamesJson.put(gameJSON);
            } else { // For both private and solo games.
                updatePrivateGames.put(gameId, gameJSON);
            }
        }
        json.put("games", gamesJson);
        cacheReference.set(json.toString());
        privateGames = updatePrivateGames;
    }

    public static String getLobby() {
        return cacheReference.get();
    }

    public static JSONObject getPrivateGame(String gameId) {
        for (String key : privateGames.keySet()) {
            System.out.println(key + " " + privateGames.get(key));
        }
        return privateGames.get(gameId);
    }

}
