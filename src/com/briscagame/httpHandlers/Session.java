package com.briscagame.httpHandlers;

import java.util.LinkedHashMap;
import java.util.UUID;

public class Session{
    public static LinkedHashMap<String, Session> sessions = new LinkedHashMap<String, Session>();
    public String username;
    public String uuid;

    public Session() {
        this("Net Player #" + (Session.sessions.size() + 1));
        register();
    }

    public Session(String username) {
        this.username = username;
        this.uuid = UUID.randomUUID().toString();
        register();
    }

    public void register() {
        sessions.put(this.uuid, this);
    }
}
