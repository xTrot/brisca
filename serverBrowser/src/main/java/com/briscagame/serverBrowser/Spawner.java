package com.briscagame.serverBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;

public class Spawner {

    public static String spawn(JSONObject gameConfig, String gameId, int port) {
        String output = null;
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(
                    EnvironmentVariable.BROWSER_GAME_SPAWNER, gameId);
            Process process = processBuilder.start();

            OutputStream stdin = process.getOutputStream();
            InputStream stdout = process.getInputStream();

            // Write to stdin
            stdin.write(gameConfig.toString().getBytes());
            stdin.flush();
            stdin.close();

            // Read from stdout
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = stdout.read(buffer)) != -1) {
                output = new String(buffer, 0, bytesRead);
                System.out.println(output);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return output;
    }

}
