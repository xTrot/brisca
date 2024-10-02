package com.briscagame.httpHandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;

public class HandlerHelper {
    private static final int NO_RESPONSE = 0;
    private static final int NOT_FOUND = 404;
    
    static String post(HttpExchange exchange) throws IOException {
        System.out.println("'"+exchange.getRequestMethod()+"'");
        if (exchange.getRequestMethod().compareTo("POST") != 0){
            exchange.sendResponseHeaders(NOT_FOUND, NO_RESPONSE);
            System.out.println("Warning: 404 on " + exchange.getRequestURI());
            OutputStream os = exchange.getResponseBody();
            os.write("".getBytes());
            os.close();
            return null;
        }
        Scanner s = new Scanner(exchange.getRequestBody());
        String result = "";
        while (s.hasNext()) {
            result+= s.next();
        }
        s.close();

        return result;
    }
}
