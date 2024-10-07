package com.briscagame;

import org.json.JSONStringer;

public class GameConfiguration {

    private boolean swapBottomCard;

    public GameConfiguration(){
        this.swapBottomCard = false;
    }

    public String toString(){
        return JSONStringer.valueToString(this);
    }

    public boolean getSwapBottomCard () {
        return this.swapBottomCard;
    }

    public void setSwapBottomCard(boolean swapBottomCard) {
        this.swapBottomCard = swapBottomCard;
    }

}
