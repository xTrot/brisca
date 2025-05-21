package com.briscagame.serverBrowser;

public class Spawner {

    public static String spawn(String port) {

        System.out.println("Spawner");
        String command = EnvironmentVariable.BROWSER_GAME_SPAWNER + " " + port;

        Script result = Script.run(command, null);

        return result.out;
    }

}
