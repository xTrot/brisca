package com.briscagame.httpHandlers;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Session{
    private static LinkedHashMap<String, AtomicReference<Session>> sessions = new LinkedHashMap<String, AtomicReference<Session>>();

    private AtomicReference<Session> thisReference;
    private final String userId;
    private String username;
    private String gameID;
    private String team;

    public static Session getSession(String userId) {
        if (!Session.sessions.containsKey(userId)) {
            return null;
        }
        return sessions.get(userId).get();
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
        this.thisReference = new AtomicReference<Session>(this);
        sessions.put(this.userId, this.thisReference);
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

    public void setUsername(String username) {
        this.thisReference.get().username = username;
    }

    public void setGameID(String gameID) {
        this.thisReference.get().gameID = gameID;
    }

    public void setTeam(String team) {
        this.thisReference.get().team = team;
    }

}
