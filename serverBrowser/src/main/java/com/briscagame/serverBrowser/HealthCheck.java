package com.briscagame.serverBrowser;

public class HealthCheck {

    public static boolean check(String gameServer,
            String retries, String retryInterval, String timeout) {

        System.out.println("Healthcheck");
        String command = EnvironmentVariable.BROWSER_GAME_HEALTHCHECK + " " +
                gameServer + " " +
                retries + " " +
                retryInterval + " " +
                timeout;

        Script result = Script.run(command, null);

        return result.exitCode == 0;

    }
}
