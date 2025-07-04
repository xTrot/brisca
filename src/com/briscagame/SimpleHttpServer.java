package com.briscagame;

// Java Program to Set up a Basic HTTP Server
import com.sun.net.httpserver.HttpServer;
import com.briscagame.httpHandlers.ActionsHandler;
import com.briscagame.httpHandlers.ChangeTeamHandler;
import com.briscagame.httpHandlers.HandHandler;
import com.briscagame.httpHandlers.JoinGameHandler;
import com.briscagame.httpHandlers.LeaveGameHandler;
import com.briscagame.httpHandlers.LobbyHandler;
import com.briscagame.httpHandlers.MakeGameHandler;
import com.briscagame.httpHandlers.PlayCardHandler;
import com.briscagame.httpHandlers.ReadyHandler;
import com.briscagame.httpHandlers.RegisterHandler;
import com.briscagame.httpHandlers.ReplayHandler;
import com.briscagame.httpHandlers.RootHandler;
import com.briscagame.httpHandlers.SeatHandler;
import com.briscagame.httpHandlers.StartGameHandler;
import com.briscagame.httpHandlers.StatusHandler;
import com.briscagame.httpHandlers.SwapBottomCardHandler;
import com.briscagame.httpHandlers.WaitingRoomHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.Executor;

// Driver Class
public class SimpleHttpServer {
    private static int port;
    private static String hostname;

    private static RootHandler rootHandler = new RootHandler();
    private static MakeGameHandler makeGameHandler = new MakeGameHandler();
    private static RegisterHandler registerHandler = new RegisterHandler();
    private static JoinGameHandler joinGameHandler = new JoinGameHandler();
    private static ChangeTeamHandler changeTeamHandler = new ChangeTeamHandler();
    private static ReadyHandler readyHandler = new ReadyHandler();
    private static LeaveGameHandler leaveGameHandler = new LeaveGameHandler();
    private static StartGameHandler startGameHandler = new StartGameHandler();
    private static PlayCardHandler playCardHandler = new PlayCardHandler();
    private static LobbyHandler lobbyHandler = new LobbyHandler();
    private static ActionsHandler actionsHandler = new ActionsHandler();
    private static HandHandler handHandler = new HandHandler();
    private static WaitingRoomHandler waitingRoomHandler = new WaitingRoomHandler();
    private static StatusHandler statusHandler = new StatusHandler();
    private static SeatHandler seatHandler = new SeatHandler();
    private static SwapBottomCardHandler swapHandler = new SwapBottomCardHandler();
    private static ReplayHandler replayHandler = new ReplayHandler();

    // Main Method
    public static void start(Executor threadPoolExecutor) throws IOException {
        String portString = Optional.ofNullable(System.getenv("HTTP_PORT")).orElse("8000");
        System.out.println("Using port: " + portString);
        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing HTTP_PORT env variable to int:");
            System.err.println("HTTP_PORT=" + portString);
            System.err.println(e);
            throw new IllegalStateException("Env variable HTTP_PORT must be an int.");
        }

        HttpServer server;
        hostname = Optional.ofNullable(System.getenv("HTTP_HOSTNAME")).orElse("0.0.0.0");
        System.out.println("Using hostname: " + hostname);
        try {
            // Create an HttpServer instance
            server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        } catch (Exception e) {
            System.err.println("Error initializing socket address:");
            System.err.println("HTTP_HOSTNAME=" + hostname);
            System.err.println("HTTP_PORT=" + port);
            System.err.println(e);
            throw new IllegalStateException("Env variables HTTP_HOSTNAME, HTTP_PORT must be able start the http server.");
        }

        // Create a context for a specific path and set the handler
        server.createContext("/", rootHandler);
        server.createContext("/makegame", makeGameHandler);
        server.createContext("/register", registerHandler);
        server.createContext("/joingame", joinGameHandler);
        server.createContext("/changeteam", changeTeamHandler);
        server.createContext("/ready", readyHandler);
        server.createContext("/leavegame", leaveGameHandler);
        server.createContext("/startgame", startGameHandler);
        server.createContext("/playcard", playCardHandler);
        server.createContext("/lobby", lobbyHandler);
        server.createContext("/actions", actionsHandler);
        server.createContext("/hand", handHandler);
        server.createContext("/waitingroom", waitingRoomHandler);
        server.createContext("/status", statusHandler);
        server.createContext("/seat", seatHandler);
        server.createContext("/swapBottomCard", swapHandler);
        server.createContext("/replay", replayHandler);

        // Start the server
        server.setExecutor(threadPoolExecutor); // Use the default executor
        server.start();

        // System.out.println("Server is running on port " + port);
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
}