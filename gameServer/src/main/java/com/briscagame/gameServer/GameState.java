package com.briscagame.gameServer;

public class GameState {

    public static enum STATE {
        SPAWNED,
        WAITING_ROOM,
        IN_PROGRESS,
        COMPLETED,
    }

    private STATE state = STATE.SPAWNED;

    public void setState(STATE state) {
        this.state = state;
    }

    public STATE getState() {
        return state;
    }

}
