package com.briscagame;

import org.json.JSONObject;

public class GameConfiguration {
    public static final int MAX_PLAYERS = 4;

    private boolean swapBottomCard;
    private int maxPlayers;

    public GameConfiguration(){
        this.swapBottomCard = false;
        this.maxPlayers = 4;
    }

    public GameConfiguration(JSONObject json){
        this.swapBottomCard = false;
        this.maxPlayers = 4;
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

}
