package com.briscagame.gameServer.handlers;

import java.util.LinkedHashMap;
import java.util.UUID;

public class Session{
    private static LinkedHashMap<String, Session> sessions = new LinkedHashMap<String, Session>();

    private final String userId;

    // Protect these with synchro
    private String username;
    private String gameID;
    private String team;
    private int actionsSent;

    public static Session getSession(String userId) {
        if (!Session.sessions.containsKey(userId)) {
            return null;
        }
        return sessions.get(userId);
    }

    public Session() {
        this("Net Player #" + (Session.sessions.size() + 1));
        register();
    }

    public Session(String username) {
        this.username = username;
        this.userId = UUID.randomUUID().toString();
        register();
    }

    public void register() {
        sessions.put(this.userId, this);
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getGameID() {
        return gameID;
    }

    public String getTeam() {
        return team;
    }

    public int getActionsSent() {
        return actionsSent;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public synchronized void setTeam(String team) {
        this.team = team;
    }

    public synchronized void setActionsSent(int actionsSent) {
        this.actionsSent = actionsSent;
    }

}
