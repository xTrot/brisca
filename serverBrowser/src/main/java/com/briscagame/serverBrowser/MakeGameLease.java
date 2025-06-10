package com.briscagame.serverBrowser;

import java.time.Instant;

import org.json.JSONObject;

public class MakeGameLease {
    private String host = EnvironmentVariable.HOSTNAME;
    private String port;
    private Instant expiration;

    public MakeGameLease(String port) {
        this.port = port;
        this.expiration = Instant.now().plusSeconds(10);
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}
