package com.briscagame.httpHandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;

public class HandlerHelper {
    private static final String NO_RESPONSE_BODY = "";
    private static final int NOT_FOUND = 404;

    static Session getSession(HttpExchange exchange) {
        String uuid = getCookie(exchange, "uuid");

        Session rtnSession = null;

        if (uuid == null) {
            rtnSession = new Session("Net Player #" + (Session.sessions.size() + 1));
        } else {

            if (Session.sessions.containsKey(uuid)) {
                return Session.sessions.get(uuid);
            } else {
                System.err.println("What, wow, how did we get here?");
                rtnSession = new Session("Net Player #" + (Session.sessions.size() + 1));
            }

        }
        setCookie(exchange, "uuid", rtnSession.uuid,true);
        return rtnSession;
    }

    public static boolean setCookie(HttpExchange exchange, String key, String setValue, boolean force) {
        String cookies = "";
        if (exchange.getRequestHeaders().containsKey("cookie")) {
            cookies = exchange.getRequestHeaders().getFirst("cookie");
        }

        String value = getCookie(exchange, key);

        if (value != null && force) {
            System.out.println("Replacing Cookie " + value + ":" + setValue);
            cookies.replace(value, setValue);
            exchange.getResponseHeaders().add("set-cookie", cookies);
            return true;
        } else if (value != null) {
            return false;
        }

        cookies += key + "=" + setValue;
        exchange.getResponseHeaders().add("set-cookie", cookies);

        return true;
    }
    
    public static String getCookie(HttpExchange exchange, String key) {
        String cookies = "";
        if (exchange.getRequestHeaders().containsKey("cookie")) {
            cookies = exchange.getRequestHeaders().getFirst("cookie");
        }

        String[] individualCookies = cookies.split(";");
        for (String cookie : individualCookies) {
            if(cookie.contains(key))
                return cookie.split("=")[1];
        }
        return null;
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
