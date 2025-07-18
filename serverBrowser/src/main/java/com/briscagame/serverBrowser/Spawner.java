package com.briscagame.serverBrowser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briscagame.serverBrowser.handlers.JoinPrivateGameHandler;

public class Spawner {

    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    public static String spawn(String port) {

        logger.debug("Spawner");
        String command = EnvironmentVariable.BROWSER_GAME_SPAWNER + " " + port;

        Script result = Script.run(command, null);

        return result.out;
    }

}
