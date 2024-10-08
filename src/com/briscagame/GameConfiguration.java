package com.briscagame;

import org.json.JSONObject;

public class GameConfiguration {

    private boolean swapBottomCard;

    public GameConfiguration(){
        this.swapBottomCard = false;
    }

    public String toString(){
        return (new JSONObject(this)).toString();
    }

    public boolean getSwapBottomCard () {
        return this.swapBottomCard;
    }

    public void setSwapBottomCard(boolean swapBottomCard) {
        this.swapBottomCard = swapBottomCard;
    }

}
