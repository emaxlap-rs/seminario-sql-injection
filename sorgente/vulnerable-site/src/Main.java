import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class Main {
    static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", (httpExchange) -> {
            Utils.sendFile(httpExchange, new File("index.html"), "text/html; charset=utf-8");
        });

        server.createContext("/login/vuln", Handlers.vulnerableLoginHandler);

        server.createContext("/login/fixed", Handlers.fixedLoginHandler);

        server.createContext("/products/vuln", Handlers.vulnerableProductsHandler);

        server.createContext("/products/fixed", Handlers.fixedProductsHandler);

        server.start();
        System.out.println("Server started on port " + PORT + "!");
    }
}