package com.briscagame;

import org.json.JSONObject;

public class PlayAction {
    public static enum ActionType {
        GAME_CONFIG,
        GAME_STARTED,
        BOTTOM_CARD_SELECTED,
        GRACE_PERIOD_ENDED,
        SWAP_BOTTOM_CARD,
        CARD_DRAWN,
        CARD_PLAYED,
        TURN_WON,
        GAME_WON
    }

    private ActionType type;
    private JSONObject payload = null;

    public PlayAction(Game game, ActionType type) {
        this(game, type, null);
    }

    public PlayAction(Game game, ActionType type, JSONObject payload) {
        this.type = type;
        this.payload = payload;
        Game.registerAction(game, this);
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
