package com.briscagame.serverBrowser;

import com.briscagame.httpHandlers.RootHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class BrowserHttpServer {

    private static RootHandler rootHandler = new RootHandler("Server Browser");

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
    }

}
