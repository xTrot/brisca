package com.briscagame.httpHandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;

public class HandlerHelper {
    private static final String NO_RESPONSE_BODY = "";
    private static final int NOT_FOUND = 404;

    static Session getSession(HttpExchange exchange) {
        String cookies = "";

        if (exchange.getRequestHeaders().containsKey("cookie")) {
            cookies = exchange.getRequestHeaders().getFirst("cookie");
        }

        String uuid = getCookie("uuid", cookies);

        Session rtnSession = null;

        if (uuid == null) {
            rtnSession = new Session("Net Player #" + (Session.sessions.size() + 1));
            cookies += "uuid=" + rtnSession.uuid + ";";
        } else {

            if (Session.sessions.containsKey(uuid)) {
                rtnSession = Session.sessions.get(uuid);
            } else {
                System.err.println("What, wow, how did we get here?");
                rtnSession = new Session("Net Player #" + (Session.sessions.size() + 1));
                cookies = "uuid=" + rtnSession.uuid + ";";
            }

        }
        exchange.getResponseHeaders().add("set-cookie", cookies);
        return rtnSession;
    }
    
    private static String getCookie(String key, String cookies) {
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
