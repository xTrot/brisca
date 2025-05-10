package com.briscagame.serverBrowser;

import java.util.Optional;

public class EnvironmentVariable {
    public static int BROWSER_PORT;
    public static String BROWSER_HOSTNAME;
    public static String BROWSER_GAME_SPAWNER;
    public static String BROWSER_GAME_HEALTHCHECK;
    public static int BROWSER_GAME_PORT_RANGE_START;
    public static int BROWSER_GAME_PORT_RANGE_COUNT;
    public static String RECORDING_DIR;

    static void load() {
        String portString = Optional.ofNullable(System.getenv("BROWSER_PORT")).orElse("9000");
        System.out.println("Using port: " + portString);
        try {
            BROWSER_PORT = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing BROWSER_PORT env variable to int:");
            System.err.println("BROWSER_PORT=" + portString);
            System.err.println(e);
            throw new IllegalStateException("Env variable BROWSER_PORT must be an int.");
        }

        BROWSER_HOSTNAME = Optional.ofNullable(System.getenv("BROWSER_HOSTNAME")).orElse("0.0.0.0");

        BROWSER_GAME_SPAWNER = Optional.ofNullable(System.getenv("BROWSER_GAME_SPAWNER")).orElse("/app/spawner");
        // TODO: Write a validator that checs the file exists and is an executable.

        BROWSER_GAME_HEALTHCHECK = Optional.ofNullable(System.getenv("BROWSER_GAME_HEALTHCHECK"))
                .orElse("/app/healthCheck");
        // TODO: Write a validator that checs the file exists and is an executable.

        BROWSER_GAME_PORT_RANGE_START = Integer
                .parseInt(Optional.ofNullable(System.getenv("BROWSER_GAME_PORT_RANGE_START")).orElse("10000"));
        // TODO: Write a validator

        BROWSER_GAME_PORT_RANGE_COUNT = Integer
                .parseInt(Optional.ofNullable(System.getenv("BROWSER_GAME_PORT_RANGE_COUNT")).orElse("1000"));
        // TODO: Write a validator

        RECORDING_DIR = Optional.ofNullable(System.getenv("RECORDING_DIR")).orElse("/app/recordings");
        // TODO: Write a validator
    }

}
