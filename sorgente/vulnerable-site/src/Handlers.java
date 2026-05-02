import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Handlers {
    public static HttpHandler indexHandler = (httpExchange) -> {
        sendText(httpExchange, "Nulla da vedere qua :)");
        httpExchange.close();
    };

    public static HttpHandler loginVuln = (httpExchange) -> {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "GET" -> {
                String query = httpExchange.getRequestURI().getQuery();
                System.out.println("GET on " + path + ": " + query);
                sendFile(httpExchange, "text/html; charset=utf-8", new File("login.html"));
            }
            case "POST" -> {
                HashMap<String, String> post = parsePost(httpExchange);
                System.out.println("Received data:");
                post.forEach((k, v) -> {
                    System.out.println(k + " " + v);
                });
                if (post.containsKey("username") &&  post.containsKey("password")) {
                    String username = post.get("username");
                    String password = post.get("password");
                    try {
                        sendJson(httpExchange, Authentication.authVuln(username, password).toJson());
                    }
                    catch (Exception e) {
                        System.err.println("Eccezione: " + e.getMessage());
                    }
                }
            }
            default -> httpExchange.sendResponseHeaders(400, 0);
        }
        httpExchange.close();
    };

    public static HttpHandler loginFixed = (httpExchange) -> {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "GET" -> {
                String query = httpExchange.getRequestURI().getQuery();
                System.out.println("GET on " + path + ": " + query);
                sendFile(httpExchange, "text/html; charset=utf-8", new File("login.html"));
            }
            case "POST" -> {
                HashMap<String, String> post = parsePost(httpExchange);
                if (post.containsKey("username") &&  post.containsKey("password")) {
                    String username = post.get("username");
                    String password = post.get("password");
                    try {
                        sendJson(httpExchange, Authentication.authFixed(username, password).toJson());
                    }
                    catch (Exception e) {
                        System.err.println("Eccezione: " + e.getMessage());
                    }
                }
            }
            default -> httpExchange.sendResponseHeaders(400, 0);
        }
        httpExchange.close();
    };

    public static HttpHandler productsVuln = (httpExchange) -> {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "GET" -> {
                String query = httpExchange.getRequestURI().getQuery();
                System.out.println("GET on " + path + ": " + query);
                sendFile(httpExchange, "text/html; charset=utf-8", new File("login.html"));
            }
            case "POST" -> {
                HashMap<String, String> post = parsePost(httpExchange);
                if (post.containsKey("username") &&  post.containsKey("password")) {
                    String username = post.get("username");
                    String password = post.get("password");
                    try {
                        sendJson(httpExchange, Authentication.authFixed(username, password).toJson());
                    }
                    catch (Exception e) {
                        System.err.println("Eccezione: " + e.getMessage());
                    }
                }
            }
            default -> httpExchange.sendResponseHeaders(400, 0);
        }
        httpExchange.close();
    };

    private static void sendJson(HttpExchange httpExchange, String json) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response);
        outputStream.flush();
        outputStream.close();
    }
    private static void sendText(HttpExchange httpExchange, String text) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response);
        outputStream.flush();
        outputStream.close();
    }
    private static void sendFile(HttpExchange httpExchange, String MIME, File file) throws IOException {
        if (!file.canRead()) {
            httpExchange.sendResponseHeaders(500, 0);
            return;
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        try {
            InputStream inputStream = new FileInputStream(file);
            long length = file.length();
            httpExchange.sendResponseHeaders(200, length);
            while (inputStream.available() > 0) {
                outputStream.write(inputStream.read());
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            httpExchange.getResponseHeaders().add("Content-Type", MIME);
        } catch (FileNotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        }
    }
    private static HashMap<String, String> parsePost(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        byte[] buf = inputStream.readAllBytes();
        inputStream.close();
        String postString = new String(buf, StandardCharsets.UTF_8);
        System.out.println("Parsing POST: " + postString);
        HashMap<String, String> kv = new HashMap<>();
        for (String s : postString.split("&")) {
            int charPos = s.indexOf("=");
            if (charPos == -1) {
                kv.put(s, "");
            }
            else {
                kv.put(s.substring(0, charPos), s.substring(charPos + 1));
            }
        }
        return kv;
    }
}
