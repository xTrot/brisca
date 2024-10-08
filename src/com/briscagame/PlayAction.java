package com.briscagame;

import org.json.JSONObject;

public class PlayAction {
    public static enum ActionType {
        GAME_SETUP_COMPLETED,
        FIRST_PLAYER_SELECTED,
        BOTTOM_CARD_SELECTED,
        CARD_DRAWN,
        CARD_PLAYED,
        TURN_WON,
        GAME_WON
    }

    private ActionType type;
    private JSONObject payload = null;

    public PlayAction(ActionType type) {
        this(type, null);
    }

    public PlayAction(ActionType type, JSONObject payload) {
        this.type = type;
        this.payload = payload;
        System.out.println("New Action of type " + this.type + " was taken.");
        System.out.println(this);
    }

    public JSONObject getPayload() {
        return this.payload;
    }

    public ActionType getType() {
        return this.type;
    }

    public String toString(){
        return (new JSONObject(this)).toString();
    }

}
