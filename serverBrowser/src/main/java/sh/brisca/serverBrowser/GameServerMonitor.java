package sh.brisca.serverBrowser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.brisca.common.GameServerState;
import sh.brisca.common.Status;
import sh.brisca.serverBrowser.handlers.JoinPrivateGameHandler;

public class GameServerMonitor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    private ThreadPoolExecutor tpe;
    public static LinkedHashMap<String, GameServerState> gameStates = new LinkedHashMap<String, GameServerState>();

    public GameServerMonitor(ThreadPoolExecutor tpe) {
        this.tpe = tpe;
        this.tpe.execute(this);
    }

    @Override
    public void run() {

        while (true) {

            LinkedHashMap<String, GameServerState> games = new LinkedHashMap<String, GameServerState>();

            LinkedHashSet<String> gameServers = BrowserPostgresConnectionPool.getAllGameServers();
            for (String server : gameServers) {

                // Create an HttpClient instance
                HttpClient client = HttpClient.newHttpClient();

                // Build an HttpRequest
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://" + server + "/gameList")) // Replace with your target URL
                        .GET() // Specify the HTTP method (GET, POST, PUT, DELETE, etc.)
                        .build();

                try {
                    // Send the request and receive the response
                    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

                    String body = response.body();

                    if (response.statusCode() != Status.OK) {
                        return;
                    }

                    JSONArray jsonArray = new JSONObject(body).getJSONArray("games");

                    for (Object object : jsonArray) {

                        JSONObject json = new JSONObject(object.toString());
                        GameServerState state = new GameServerState(json);
                        games.put(state.getGameConfiguration().getGameId(), state);

                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    logger.info("Removing server: {}", server);
                    BrowserPostgresConnectionPool.removeGameServer(server);
                } catch (Exception e) {
                    logger.error("Unknown Error", e);
                    e.printStackTrace();
                }

            }

            gameStates = games;
            Lobby.updateLobby();

            try {
                Thread.sleep(EnvironmentVariable.BROWSER_GAMELIST_REFRESH);
            } catch (InterruptedException e) {
                logger.error("{}", e);
            }

        }

    }

}
