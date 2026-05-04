import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {
    static final int PORT = 8000;

    public static void main(String[] args) throws IOException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", (httpExchange) -> {
            Utils.sendText(httpExchange, "Nulla da vedere qua :)");
            httpExchange.close();
        });

        server.createContext("/login/vuln", Login.vulnerableSiteHandler);

        server.createContext("/login/fixed", Login.fixedSiteHandler);

        server.createContext("/products/vuln");

        server.start();
        System.out.println("Server started on port " + PORT + "!");
    }
}