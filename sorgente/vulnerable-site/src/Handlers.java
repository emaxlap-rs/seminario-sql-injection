import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Handlers {
    public static HttpHandler indexHandler = (httpExchange) -> {
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        byte[] response = "Nulla da vedere qua :)".getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response);
        outputStream.flush();
        outputStream.close();
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
                InputStream inputStream = httpExchange.getRequestBody();
                byte[] buf = inputStream.readAllBytes();
                inputStream.close();
                String req = new String(buf, StandardCharsets.UTF_8);
                System.out.println("POST on " + path + ": " + req);
                
            }
            default -> httpExchange.sendResponseHeaders(400, 0);
        }

        httpExchange.close();
    };

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
}
