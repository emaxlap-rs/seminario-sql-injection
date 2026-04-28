import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", httpExchange -> {
            // Stampa a schermo gli headers
            httpExchange.getRequestHeaders().forEach((key, value) -> {
                System.out.println(key + ": " + value);
            });

            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String req = switch (method) {
                case "GET" -> {
                    String query = httpExchange.getRequestURI().getQuery();
                    yield query == null ? "" : query;
                }
                case "POST" -> {
                    InputStream inputStream = httpExchange.getRequestBody();
                    byte[] buf = inputStream.readAllBytes();
                    inputStream.close();
                    yield new String(buf, StandardCharsets.UTF_8);
                }
                default -> {
                    httpExchange.sendResponseHeaders(405, 0);
                    httpExchange.close();
                    yield null;
                }
            };

            if (req == null) {
                return;
            }

            System.out.println(path + " " + method + " " + req + "\n");

            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();
        });

        server.start();
        System.out.println("Server started on port 8000!");
    }
}