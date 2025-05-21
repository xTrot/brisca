package com.briscagame.serverBrowser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONObject;

import com.briscagame.httpHandlers.GameServerState;
import com.briscagame.httpHandlers.GameState;

public class GameServerPool implements Runnable {
    public static GameServerState[] gameServers;
    public static String[] diffStrings;
    public static int nextSpawnTry = 0;
    public static int spawnedCount = 0;
    private ThreadPoolExecutor tpe;

    public GameServerPool(ThreadPoolExecutor tpe) {
        gameServers = new GameServerState[EnvironmentVariable.BROWSER_GAME_PORT_RANGE_COUNT];
        diffStrings = new String[EnvironmentVariable.BROWSER_GAME_PORT_RANGE_COUNT];
        Arrays.fill(diffStrings, "");
        this.tpe = tpe;
        this.tpe.execute(this);
    }

    public void trySpawn() {
        String port = Integer.toString(EnvironmentVariable.BROWSER_GAME_PORT_RANGE_START +
                nextSpawnTry % EnvironmentVariable.BROWSER_GAME_PORT_RANGE_COUNT);
        Spawner.spawn(port);

        nextSpawnTry++;
    }

    @Override
    public void run() {
        for (int i = 0; i < EnvironmentVariable.BROWSER_TARGET_SERVER_POOL; i++) {
            trySpawn();
        }

        while (true) {
            try {
                Thread.sleep(1000);
                monitorServers();
                topOff();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void topOff() {
        int count = EnvironmentVariable.BROWSER_TARGET_SERVER_POOL - spawnedCount;
        for (int i = 0; i < count; i++) {
            trySpawn();
        }
    }

    private void monitorServers() {
        HttpClient.Builder hcb = HttpClient.newBuilder();
        hcb.connectTimeout(Duration.ofMillis(200));
        HttpClient client = hcb.build();
        boolean dirty = false;
        int spawnedCurrent = 0;

        for (int i = 0; i < EnvironmentVariable.BROWSER_GAME_PORT_RANGE_COUNT; i++) {
            String port = Integer.toString(EnvironmentVariable.BROWSER_GAME_PORT_RANGE_START +
                    i % EnvironmentVariable.BROWSER_GAME_PORT_RANGE_COUNT);

            String HOSTNAME_TEMP = "127.0.0.1";

            StringBuilder sb = new StringBuilder();
            sb.append("http://");
            sb.append(HOSTNAME_TEMP);
            sb.append(":");
            sb.append(port);
            sb.append("/state");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(sb.toString()))
                    .build();

            // Synchronous request
            HttpResponse<String> response;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                continue;
            }

            String json = response.body();
            if (!diffStrings[i].equals(json)) {
                System.out.println("Server: " + port + " Update: " + json);
                dirty = true;
            }
            diffStrings[i] = json;
            JSONObject parsedJson = new JSONObject(json);
            GameServerState state = new GameServerState(parsedJson);
            gameServers[i] = state;

            switch (state.getState()) {
                case GameState.SPAWNED:
                    spawnedCurrent++;
                    break;

                case GameState.WAITING_ROOM:
                    break;

                case GameState.IN_PROGRESS:
                    break;

                case GameState.COMPLETED:
                    break;

                default:
                    break;
            }

        }

        spawnedCount = spawnedCurrent;

        if (dirty) {
            Lobby.updateLobby();
        }
    }

}
