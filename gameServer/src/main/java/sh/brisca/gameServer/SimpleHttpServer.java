package sh.brisca.gameServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Java Program to Set up a Basic HTTP Server
import com.sun.net.httpserver.HttpServer;

import sh.brisca.common.PostgresConnectionPool;
import sh.brisca.common.RegisterHandler;
import sh.brisca.common.RootHandler;
import sh.brisca.common.StatusHandler;
import sh.brisca.gameServer.handlers.ActionsHandler;
import sh.brisca.gameServer.handlers.ChangeTeamHandler;
import sh.brisca.gameServer.handlers.MakeGameHandler;
import sh.brisca.gameServer.handlers.GameListHandler;
import sh.brisca.gameServer.handlers.HandHandler;
import sh.brisca.gameServer.handlers.JoinGameHandler;
import sh.brisca.gameServer.handlers.LeaveGameHandler;
import sh.brisca.gameServer.handlers.LobbyHandler;
import sh.brisca.gameServer.handlers.PlayCardHandler;
import sh.brisca.gameServer.handlers.ReadyHandler;
import sh.brisca.gameServer.handlers.SeatHandler;
import sh.brisca.gameServer.handlers.StartGameHandler;
import sh.brisca.gameServer.handlers.SwapBottomCardHandler;
import sh.brisca.gameServer.handlers.WaitingRoomHandler;

// Driver Class
public class SimpleHttpServer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpServer.class);

    private static HttpServer server;
    private static int port;
    private static String hostname;

    private static RootHandler rootHandler = new RootHandler("Game Server");
    private static JoinGameHandler joinGameHandler = new JoinGameHandler();
    private static ChangeTeamHandler changeTeamHandler = new ChangeTeamHandler();
    private static ReadyHandler readyHandler = new ReadyHandler();
    private static LeaveGameHandler leaveGameHandler = new LeaveGameHandler();
    private static StartGameHandler startGameHandler = new StartGameHandler();
    private static PlayCardHandler playCardHandler = new PlayCardHandler();
    private static ActionsHandler actionsHandler = new ActionsHandler();
    private static HandHandler handHandler = new HandHandler();
    private static WaitingRoomHandler waitingRoomHandler = new WaitingRoomHandler();
    private static StatusHandler statusHandler = new StatusHandler();
    private static SeatHandler seatHandler = new SeatHandler();
    private static SwapBottomCardHandler swapHandler = new SwapBottomCardHandler();
    private static MakeGameHandler makeGameHandler = new MakeGameHandler();
    private static RegisterHandler registerHandler = new RegisterHandler();
    private static LobbyHandler lobbyHandler = new LobbyHandler();
    private static GameListHandler gameListHandler = new GameListHandler();

    // Main Method
    public static void start(Executor threadPoolExecutor) throws IOException {
        String portString = Optional.ofNullable(System.getenv("GAME_PORT")).orElse("8000");
        logger.info("Using port: {}", portString);
        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            logger.error("Error parsing GAME_PORT env variable to int:");
            logger.error("GAME_PORT={}", portString);
            logger.error("{}", e);
            throw new IllegalStateException("Env variable GAME_PORT must be an int.");
        }

        hostname = Optional.ofNullable(System.getenv("GAME_HOSTNAME")).orElse("0.0.0.0");
        logger.info("Using hostname: {}", hostname);
        try {
            // Create an HttpServer instance
            server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        } catch (Exception e) {
            logger.error("Error initializing socket address:");
            logger.error("GAME_HOSTNAME={}", hostname);
            logger.error("GAME_PORT={}", port);
            logger.error("{}", e);
            throw new IllegalStateException(
                    "Env variables GAME_HOSTNAME, GAME_PORT must be able start the http server.");
        }

        String HOSTNAME;
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("Won't start http server.");
            e.printStackTrace();
            return;
        }

        hostname = HOSTNAME;

        logger.info("Server: {}:{}", HOSTNAME, portString);

        // Create a context for a specific path and set the handler
        server.createContext("/", rootHandler);
        server.createContext("/joingame", joinGameHandler);
        server.createContext("/changeteam", changeTeamHandler);
        server.createContext("/ready", readyHandler);
        server.createContext("/leavegame", leaveGameHandler);
        server.createContext("/startgame", startGameHandler);
        server.createContext("/playcard", playCardHandler);
        server.createContext("/actions", actionsHandler);
        server.createContext("/hand", handHandler);
        server.createContext("/waitingroom", waitingRoomHandler);
        server.createContext("/status", statusHandler);
        server.createContext("/seat", seatHandler);
        server.createContext("/swapBottomCard", swapHandler);
        server.createContext("/makeGame", makeGameHandler);
        server.createContext("/register", registerHandler);
        server.createContext("/lobby", lobbyHandler);
        server.createContext("/gameList", gameListHandler);

        // Start the server
        server.setExecutor(threadPoolExecutor); // Use the default executor
        PostgresConnectionPool.initDataSource();
        server.start();

        logger.info("Registering gameServer: {}", hostname);
        GamePostgresConnectionPool.registerGameServer(hostname + ":" + port);

        Runtime.getRuntime().addShutdownHook(new ShutdownCleanUp());
        logger.info("Shutdown hook activated.");

    }

    public static PlayCardHandler getPlayCardHandler() {
        return playCardHandler;
    }

    public static JoinGameHandler getJoinGameHandler() {
        return joinGameHandler;
    }

    public static ReadyHandler getReadyHandler() {
        return readyHandler;
    }

    public static void stop() {
        server.stop(5);
    }

    public static int getPort() {
        return port;
    }

    public static String getHostname() {
        return hostname;
    }

    public static void removeStatusContext() {
        server.removeContext("/status");
    }

    public static void removeMakeGameContext() {
        server.removeContext("/makeGame");
    }

}
