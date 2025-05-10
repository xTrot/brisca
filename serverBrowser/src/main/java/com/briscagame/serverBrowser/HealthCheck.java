package com.briscagame.serverBrowser;

import java.io.IOException;
import java.io.InputStream;

public class HealthCheck {

    public static boolean check(String gameServer,
            String retries, String retryInterval, String timeout) {

        try {

            ProcessBuilder processBuilder = new ProcessBuilder(
                    EnvironmentVariable.BROWSER_GAME_HEALTHCHECK,
                    gameServer,
                    retries,
                    retryInterval,
                    timeout);

            Process process = processBuilder.start();

            InputStream stdout = process.getInputStream();

            // Read from stdout
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = stdout.read(buffer)) != -1) {
                System.out.println(new String(buffer, 0, bytesRead));
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code : " + exitCode);

            return exitCode == 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
