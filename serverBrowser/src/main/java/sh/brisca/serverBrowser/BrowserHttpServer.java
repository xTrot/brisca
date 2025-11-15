package sh.brisca.serverBrowser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpServer;

import sh.brisca.common.PostgresConnectionPool;
import sh.brisca.common.RegisterHandler;
import sh.brisca.common.RootHandler;
import sh.brisca.serverBrowser.handlers.JoinPrivateGameHandler;
import sh.brisca.serverBrowser.handlers.LobbyHandler;
import sh.brisca.serverBrowser.handlers.RefreshHandler;
import sh.brisca.serverBrowser.handlers.ReplayHandler;

public class BrowserHttpServer {

    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    private static RootHandler rootHandler = new RootHandler("Server Browser");
    private static RegisterHandler registerHandler = new RegisterHandler();
    private static ReplayHandler replayHandler = new ReplayHandler();
    private static LobbyHandler lobbyHandler = new LobbyHandler();
    private static JoinPrivateGameHandler joinPrivateGameHandler = new JoinPrivateGameHandler();
    private static RefreshHandler refreshHandler = new RefreshHandler();

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
            logger.error("Error initializing socket address:");
            logger.error("BROWSER_HOSTNAME={}", hostname);
            logger.error("BROWSER_PORT={}", port);
            logger.error("{}", e);
            throw new IllegalStateException(
                    "Env variables BROWSER_HOSTNAME, BROWSER_PORT must be able start the http server.");
        }

        // Create a context for a specific path and set the handler
        server.createContext("/", rootHandler);
        server.createContext("/register", registerHandler);
        server.createContext("/replay", replayHandler);
        server.createContext("/lobby", lobbyHandler);
        server.createContext("/joinprivategame", joinPrivateGameHandler);
        server.createContext("/refresh", refreshHandler);

        // Start the server
        server.setExecutor(threadPoolExecutor); // Use the default executor
        PostgresConnectionPool.initDataSource();
        PostgresConnectionPool.getActiveSessions();
        server.start();

    }

}
