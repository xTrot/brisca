package com.briscagame;

// Java Program to Set up a Basic HTTP Server
import com.sun.net.httpserver.HttpServer;
import com.briscagame.httpHandlers.PlayCard;
import com.briscagame.httpHandlers.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// Driver Class
public class SimpleHttpServer 
{
    private static int port = 8000;
    private static PlayCard playCardHandler = new PlayCard();
    // Main Method
    public static void start(String[] args) throws IOException
    {
        // Create an HttpServer instance
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Create a context for a specific path and set the handler
        server.createContext("/", new RootHandler());
        server.createContext("/playcard", playCardHandler);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newCachedThreadPool();

        // Start the server
        server.setExecutor(threadPoolExecutor); // Use the default executor
        server.start();

        System.out.println("Server is running on port " + port);
    }

    public static PlayCard getPlayCardHandler() {
        return playCardHandler;
    }
}