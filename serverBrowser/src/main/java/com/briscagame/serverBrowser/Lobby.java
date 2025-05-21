package com.briscagame.serverBrowser;

import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONArray;
import org.json.JSONObject;

import com.briscagame.httpHandlers.GameServerState;

public class Lobby {
    private static AtomicReference<String> cacheReference = new AtomicReference<String>("[]");

    public static void updateLobby() {
        JSONObject json = new JSONObject();
        JSONArray gamesJson = new JSONArray();
        for (GameServerState state : GameServerPool.gameServers) {
            if (state == null || !state.isPublic() || state.hasStarted()) {
                continue;
            }
            String fill = state.getFill();
            JSONObject gameJSON = new JSONObject();
            gameJSON.put("gameId", state.getGameConfiguration().gameId);
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
