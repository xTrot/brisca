package com.briscagame;

// Java Program to Set up a Basic HTTP Server
import com.sun.net.httpserver.HttpServer;
import com.briscagame.httpHandlers.PlayCardHandler;
import com.briscagame.httpHandlers.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

// Driver Class
public class SimpleHttpServer 
{
    private static int port = 8000;
    private static RootHandler rootHandler = new RootHandler();
    private static PlayCardHandler playCardHandler = new PlayCardHandler();
    // Main Method
    public static void start(Executor threadPoolExecutor) throws IOException
    {
        // Create an HttpServer instance
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Create a context for a specific path and set the handler
        server.createContext("/", rootHandler);
        server.createContext("/playcard", playCardHandler);

        // Start the server
        server.setExecutor(threadPoolExecutor); // Use the default executor
        server.start();

        // System.out.println("Server is running on port " + port);
    }

    public static PlayCardHandler getPlayCardHandler() {
        return playCardHandler;
    }
}