package com.briscagame.httpHandlers;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

public class Session {
    private static LinkedHashMap<String, Session> sessions = new LinkedHashMap<String, Session>();

    private final String userId;

    // Protect these with synchro
    private String username;
    private String gameID;
    private String team;
    private int actionsSent;
    private Timestamp refreshBy;

    public static Session getSession(String userId) {
        Session session = Session.sessions.get(userId);
        if (session != null) {
            return session;
        }

        System.out.println("Couldn't find it here, checking db.");

        return PostgresConnectionPool.getSession(userId);
    }

    public Session() {
        this("Net Player #" + (Session.sessions.size() + 1));
    }

    public Session(String username) {
        this.username = username;
        this.userId = PostgresConnectionPool.createGuestSession(this, username);
        register();
    }

    public Session(String token, Timestamp refreshBy, String username) {
        this.userId = token;
        this.refreshBy = refreshBy;
        this.username = username;
        register();
    }

    private void register() {
        System.out.println("Resgitered: " + userId);
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

    public Timestamp getRefreshBy() {
        return refreshBy;
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

    public void setRefreshBy(Timestamp refreshBy) {
        this.refreshBy = refreshBy;
    }

}
