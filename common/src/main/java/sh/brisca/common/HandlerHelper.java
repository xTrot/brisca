package sh.brisca.common;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

public class HandlerHelper {
    private static final String NO_RESPONSE_BODY = "";
    private static final String RFC1123_GOLANG = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private static final Logger logger = LoggerFactory.getLogger(HandlerHelper.class);

    public static LinkedHashMap<String, String> getCookies(HttpExchange exchange) {
        LinkedHashMap<String, String> cookiesHashMap = new LinkedHashMap<String, String>();
        String cookies = "";
        if (exchange.getRequestHeaders().containsKey("cookie")) {
            cookies = exchange.getRequestHeaders().getFirst("cookie");
        }

        String[] individualCookies = cookies.split("; ");
        for (String cookie : individualCookies) {
            if (cookie.contains("=")) {
                String[] kvPair = cookie.split("=");
                cookiesHashMap.put(kvPair[0], kvPair[1]);
            }
        }
        return cookiesHashMap;
    }

    public static String getCookie(HttpExchange exchange, String key) {
        String cookies = "";
        String cookie = null;
        if (!exchange.getRequestHeaders().containsKey("cookie")) {
            return cookie;
        }
        cookies = exchange.getRequestHeaders().getFirst("cookie");
        int keyIndex = cookies.indexOf(key);
        if (keyIndex < 0) {
            return cookie;
        }
        int eqIndex = cookies.indexOf("=", keyIndex);
        eqIndex += 1;
        if (eqIndex < 0) {
            return cookie;
        }
        int semiIndex = cookies.indexOf(";", eqIndex);
        if (semiIndex < 0) {
            cookie = cookies.substring(eqIndex, cookies.length());
            return cookie;
        }

        cookie = cookies.substring(eqIndex, semiIndex);

        return cookie;
    }

    public static void setCookie(HttpExchange exchange, String key, String value, Timestamp refreshby) {
        StringBuilder cookie = new StringBuilder();
        cookie.append(key);
        cookie.append("=");
        cookie.append(value);
        cookie.append("; Expires=");
        Instant instant = refreshby.toInstant();
        ZonedDateTime gmtInstant = instant.atZone(ZoneId.of("GMT"));
        // DateTimeFormatter.RFC_1123_DATE_TIME uses "EEE, d MMM yyyy HH:mm:ss zzz"
        // instead of "EEE, dd MMM yyyy HH:mm:ss zzz" it was missing a d for the date.
        cookie.append(gmtInstant.format(DateTimeFormatter.ofPattern(RFC1123_GOLANG, Locale.ENGLISH)));
        cookie.append(";");
        exchange.getResponseHeaders().add("set-cookie", cookie.toString());
    }

    public static void setCookies(HttpExchange exchange, LinkedHashMap<String, String> cookiesHashMap) {
        StringBuilder cookie = new StringBuilder();
        for (String key : cookiesHashMap.keySet()) {
            cookie.append(key);
            cookie.append("=");
            cookie.append(cookiesHashMap.get(key));
            cookie.append(";");
            exchange.getResponseHeaders().add("set-cookie", cookie.toString());
            cookie.setLength(0);// Clear
            // One cookie per set-cookie header, what?
            // see:https://httpwg.org/specs/rfc6265.html#sane-set-cookie-syntax
        }
    }

    public static void getMethod(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().compareTo("GET") != 0) {
            logger.warn("Warning: 404 on {}", exchange.getRequestURI());
            sendStatus(exchange, Status.NOT_FOUND);
            return;
        }
    }

    public static HashMap<String, String> getParams(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = new HashMap<String, String>();
        String allParams = exchange.getRequestURI().toString().split("\\?")[1];
        String[] allParamsArray = allParams.split("&");
        for (String string : allParamsArray) {
            String[] kvPair = string.split("=");
            params.put(kvPair[0], kvPair[1]);
        }

        return params;
    }

    public static String postMethod(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().compareTo("POST") != 0) {
            logger.warn("Warning: 404 on {}", exchange.getRequestURI());
            sendStatus(exchange, Status.NOT_FOUND);
            return null;
        }
        Scanner s = new Scanner(exchange.getRequestBody());
        String result = "";
        while (s.hasNext()) {
            result += s.next();
        }
        s.close();

        return result;
    }

    public static void sendResponse(HttpExchange x, int code, String response) throws IOException {
        x.sendResponseHeaders(code, response.length());
        OutputStream os = x.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void sendStatus(HttpExchange x, int code) throws IOException {
        sendResponse(x, code, NO_RESPONSE_BODY);
    }

    public static boolean refresh(HttpExchange exchange, Session userSession) {
        if (!PostgresConnectionPool.refreshSession(userSession)) {
            return false;
        }
        HandlerHelper.setCookie(exchange, "userId", userSession.getUserId(), userSession.getRefreshBy());
        return true;
    }
}
