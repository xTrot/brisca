package com.briscagame;

import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class GameConfiguration {
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final List<String> GAME_TYPE_STRINGS = List.of("solo", "public", "private");
    public static final int SOLO = 0;
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;

    boolean swapBottomCard;
    int maxPlayers;
    String gameType;
    String gameId = UUID.randomUUID().toString();

    public GameConfiguration() {
        this.swapBottomCard = false;
        this.maxPlayers = 4;
        this.gameType = GAME_TYPE_STRINGS.get(SOLO);
    }

    public GameConfiguration(JSONObject json) {
        this.swapBottomCard = json.getBoolean("swapBottomCard");
        this.maxPlayers = json.getInt("maxPlayers");
        this.gameType = json.getString("gameType");
        if (this.maxPlayers > MAX_PLAYERS || this.maxPlayers < MIN_PLAYERS) {
            throw new JSONException("maxPlayers out of bounds.");
        }
        if (!GAME_TYPE_STRINGS.contains(this.gameType)) {
            throw new JSONException("gameType not valid.");
        }
    }

    public String toString() {
        return (new JSONObject(this)).toString();
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public boolean getSwapBottomCard() {
        return this.swapBottomCard;
    }

    public void setSwapBottomCard(boolean swapBottomCard) {
        this.swapBottomCard = swapBottomCard;
    }

    public String getGameType() {
        return gameType;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public boolean isSoloGame() {
        return this.gameType.equals(GAME_TYPE_STRINGS.get(SOLO));
    }

}
