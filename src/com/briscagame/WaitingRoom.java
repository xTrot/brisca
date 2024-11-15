package com.briscagame;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONArray;
import org.json.JSONObject;

public class WaitingRoom {
    private AtomicReference<String> cacheReference = new AtomicReference<String>(null);
    private Game game;

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

        json.put("players", playersJson);
        json.put("fill", fill);

        cacheReference.set(json.toString());
    }

    public String getWaitingRoom() {
        return cacheReference.get();
    }

}
