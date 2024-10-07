package com.briscagame;

import org.json.JSONStringer;

public class PlayAction {
    public static enum ActionType {
        GAME_CONFIGURATION,
        DRAW_CARD
    }

    private ActionType type;
    private String payload;

    public PlayAction(ActionType type) {
        this.type = type;
        System.out.println("New Action of type " + this.type + "was taken.");
    }

    public void setPayload(String payload) {
        this.payload = payload;
        System.out.println(this);
    }

    public String getPayload() {
        return this.payload;
    }

    public ActionType getType() {
        return this.type;
    }

    public String toString(){
        return JSONStringer.valueToString(this);
    }

}
