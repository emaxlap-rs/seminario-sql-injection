import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {
    static final int PORT = 8000;

    public static void main(String[] args) throws IOException, SQLException {
        Authentication.init();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", Handlers.indexHandler);

        server.createContext("/login/vuln", Handlers.loginVuln);

        server.createContext("/login/fixed", Handlers.loginFixed);

        server.createContext("/products/vuln");

        server.start();
        System.out.println("Server started on port 8000!");
    }
}