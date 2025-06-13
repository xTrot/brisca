package com.briscagame.serverBrowser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import com.briscagame.httpHandlers.PostgresConnectionPool;
import com.briscagame.httpHandlers.RegisterHandler;
import com.briscagame.httpHandlers.RootHandler;
import com.briscagame.serverBrowser.handlers.JoinPrivateGameHandler;
import com.briscagame.serverBrowser.handlers.LeaseHandler;
import com.briscagame.serverBrowser.handlers.LobbyHandler;
import com.briscagame.serverBrowser.handlers.ReplayHandler;
import com.sun.net.httpserver.HttpServer;

public class BrowserHttpServer {

    private static RootHandler rootHandler = new RootHandler("Server Browser");
    private static RegisterHandler registerHandler = new RegisterHandler();
    private static ReplayHandler replayHandler = new ReplayHandler();
    private static LobbyHandler lobbyHandler = new LobbyHandler();
    private static LeaseHandler leaseHandler = new LeaseHandler();
    private static JoinPrivateGameHandler joinPrivateGameHandler = new JoinPrivateGameHandler();

    // Main Method
    public static void start(Executor threadPoolExecutor) throws IOException {

        EnvironmentVariable.load();

        String hostname = EnvironmentVariable.BROWSER_HOSTNAME;
        int port = EnvironmentVariable.BROWSER_PORT;

        HttpServer server;
        try {
            // Create an HttpServer instance
            server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        } catch (Exception e) {
            System.err.println("Error initializing socket address:");
            System.err.println("BROWSER_HOSTNAME=" + hostname);
            System.err.println("BROWSER_PORT=" + port);
            System.err.println(e);
            throw new IllegalStateException(
                    "Env variables BROWSER_HOSTNAME, BROWSER_PORT must be able start the http server.");
        }

        // Create a context for a specific path and set the handler
        server.createContext("/", rootHandler);
        server.createContext("/register", registerHandler);
        server.createContext("/replay", replayHandler);
        server.createContext("/lobby", lobbyHandler);
        server.createContext("/lease", leaseHandler);
        server.createContext("/joinprivategame", joinPrivateGameHandler);

        // Start the server
        server.setExecutor(threadPoolExecutor); // Use the default executor
        PostgresConnectionPool.initDataSource();
        PostgresConnectionPool.getActiveSessions();
        server.start();

    }

}
