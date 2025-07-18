package com.briscagame.serverBrowser;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briscagame.httpHandlers.GameServerState;
import com.briscagame.httpHandlers.GameState;
import com.briscagame.serverBrowser.handlers.JoinPrivateGameHandler;

public class LeasingOffice implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    private HashMap<String, MakeGameLease> leases = new HashMap<String, MakeGameLease>();

    public LeasingOffice(ThreadPoolExecutor tpe) {
        tpe.execute(this);
    }

    public MakeGameLease getLease(String userId) {
        // MakeGameLease lease;
        MakeGameLease lease = leases.get(userId);
        if (lease != null) {
            logger.info("Existing Lease: {}", new JSONObject(lease));
            return lease;
        }

        logger.info("No exisiting lease.");

        String port = null;
        int server = 0;
        GameServerState gsState = null;
        for (GameServerState state : GameServerPool.gameServers) {
            if (state != null && state.getState() == GameState.SPAWNED) {
                gsState = state;
                port = Integer.toString(EnvironmentVariable.BROWSER_GAME_PORT_RANGE_START + server);
                break;
            }
            server++;
        }

        if (port == null) {
            return null;
        }

        logger.info("Query db to make lease for {}: {}", userId, gsState.server);

        if (!BrowserPostgresConnectionPool.newGameLease(userId, gsState.server)) {
            logger.error("Failed to create db lease.");
            return null;
        }

        lease = new MakeGameLease(port);
        leases.put(userId, lease);

        logger.info("New Lease: {}", new JSONObject(lease));

        return lease;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Duration.ofMillis(200));
            } catch (InterruptedException e) {
                logger.error("{}", e);
                break;
            }
            monitorExpiration();
        }
    }

    private void monitorExpiration() {
        Instant now = Instant.now();
        for (String userId : leases.keySet()) {
            MakeGameLease lease = leases.get(userId);
            if (now.isAfter(lease.getExpiration())) {
                leases.remove(userId);
            }
        }
    }
}
