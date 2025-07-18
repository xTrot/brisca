package com.briscagame.gameServer;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        GAME_WON,
        SEAT_AFK,
        SEAT_NOT_AFK,
    }

    private static final Logger logger = LoggerFactory.getLogger(PlayAction.class);

    private ActionType type;
    private JSONObject payload = null;

    public PlayAction(Game game, ActionType type) {
        this(game, type, null);
    }

    public PlayAction(Game game, ActionType type, JSONObject payload) {
        this.type = type;
        this.payload = payload;
        Game.registerAction(game, this);
        logger.info("{}", this);
    }

    public JSONObject getPayload() {
        return this.payload;
    }

    public ActionType getType() {
        return this.type;
    }

    public String toString() {
        return (new JSONObject(this)).toString();
    }

}
