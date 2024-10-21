package com.briscagame;

import org.json.JSONObject;

public class GameConfiguration {
    public static final int MAX_PLAYERS = 4;

    boolean swapBottomCard;
    int maxPlayers;
    String gameType;

    public GameConfiguration(){
        this.swapBottomCard = false;
        this.maxPlayers = 4;
        this.gameType = "solo";
    }

    public GameConfiguration(JSONObject json){
        this.swapBottomCard = json.getBoolean("swapBottomCard");
        this.maxPlayers = json.getInt("maxPlayers");
        this.gameType = json.getString("gameType");
    }

    public String toString(){
        return (new JSONObject(this)).toString();
    }

    public int getMaxPlayers () {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean getSwapBottomCard () {
        return this.swapBottomCard;
    }

    public void setSwapBottomCard(boolean swapBottomCard) {
        this.swapBottomCard = swapBottomCard;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

}
