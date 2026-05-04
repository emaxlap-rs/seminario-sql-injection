import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Utils {
    public static void sendBytes(HttpExchange httpExchange, byte[] data) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }
    public static void sendBytes(HttpExchange httpExchange, byte[] data, String mimeType) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", mimeType);
        sendBytes(httpExchange, data);
    }
    public static void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        sendBytes(httpExchange, buffer, "text/plain; charset=utf-8");
    }
    public static void sendJson(HttpExchange httpExchange, String json) throws IOException {
        sendBytes(httpExchange, json.getBytes(StandardCharsets.UTF_8), "application/json; charset=utf-8");
    }
    public static void sendFile(HttpExchange httpExchange, File file, String mimeType) throws IOException {
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
            httpExchange.getResponseHeaders().add("Content-Type", mimeType);
        } catch (FileNotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        }
    }
    private static HashMap<String, String> buildHashMap(String queryString) {
        HashMap<String, String> res = new HashMap<>();
        for (String s : queryString.split("&")) {
            int charPos = s.indexOf("=");
            if (charPos == -1) {
                res.put(s, "");
            }
            else {
                res.put(s.substring(0, charPos), s.substring(charPos + 1));
            }
        }
        return res;
    }
    public static HashMap<String, String> parsePost(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String postString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        inputStream.close();
        return buildHashMap(postString);
    }
}
