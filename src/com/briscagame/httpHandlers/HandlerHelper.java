package com.briscagame.httpHandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;

public class HandlerHelper {
    private static final String NO_RESPONSE_BODY = "";
    private static final int NOT_FOUND = 404;

    static Session getSession(HttpExchange exchange) {
        LinkedHashMap<String, String> cookies = getCookies(exchange);
        Session rtnSession = null;
        if (!cookies.containsKey("userId")) {
            return rtnSession;
        }
        String userId = cookies.get("userId");
        if (!Session.sessions.containsKey(userId)) {
            return rtnSession;
        }
        rtnSession = Session.sessions.get(userId);
        return rtnSession;
    }
    
    public static LinkedHashMap<String,String> getCookies(HttpExchange exchange) {
        LinkedHashMap<String,String> cookiesHashMap = new LinkedHashMap<String,String>();
        String cookies = "";
        if (exchange.getRequestHeaders().containsKey("cookie")) {
            cookies = exchange.getRequestHeaders().getFirst("cookie");
        }

        String[] individualCookies = cookies.split(";");
        for (String cookie : individualCookies) {
            if (cookie.contains("=")){
                String[] kvPair = cookie.split("=");
                cookiesHashMap.put(kvPair[0], kvPair[1]);
            }
        }
        return cookiesHashMap;
    }
    
    public static void setCookies(HttpExchange exchange, LinkedHashMap<String,String> cookiesHashMap) {
        StringBuilder cookies = new StringBuilder();
        for (String key : cookiesHashMap.keySet()) {
            cookies.append(key);
            cookies.append("=");
            cookies.append(cookiesHashMap.get(key));
            cookies.append(";");
        }
        exchange.getResponseHeaders().add("set-cookie", cookies.toString());
    }

    static String post(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().compareTo("POST") != 0){
            // System.out.println("Warning: 404 on " + exchange.getRequestURI());
            sendStatus(exchange, NOT_FOUND);
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

    static void sendResponse(HttpExchange x, int code, String response) throws IOException {
        x.sendResponseHeaders(code, response.length());
        OutputStream os = x.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    static void sendStatus(HttpExchange x, int code) throws IOException{
        sendResponse(x, code, NO_RESPONSE_BODY);
    }
}
