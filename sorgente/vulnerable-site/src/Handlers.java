import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Handlers {
    public static HttpHandler indexHandler = (httpExchange) -> {
        Utils.sendFile(httpExchange, new File("index.html"), "text/html; charset=utf-8");
        httpExchange.close();
    };
    public static HttpHandler vulnerableLoginHandler = (httpExchange) -> handleLogin(httpExchange, false);
    public static HttpHandler fixedLoginHandler = (httpExchange) -> handleLogin(httpExchange, true);
    public static HttpHandler vulnerableProductsHandler = (httpExchange) -> handleProducts(httpExchange, false);
    public static HttpHandler fixedProductsHandler = (httpExchange) -> handleProducts(httpExchange, true);

    private static void handleLogin(HttpExchange httpExchange, boolean safe) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch (method) {
            case "GET":
                Utils.sendFile(httpExchange, new File("login.html"), "text/html; charset=utf-8");
                break;
            case "POST":
                HashMap<String, String> postData = Utils.parsePost(httpExchange);
                if (postData.containsKey("username") &&  postData.containsKey("password")) {
                    String username = postData.get("username");
                    String password = postData.get("password");
                    try {
                        String login = (safe ? Critical.authFixed(username, password) : Critical.authVuln(username, password));
                        String ans = "{ \"ok\": " + !login.isEmpty() + ", \"username\": \"" + login + "\" }";
                        Utils.sendJson(httpExchange, ans);
                    }
                    catch (Exception e) {
                        System.err.println("Eccezione: " + e.getMessage());
                    }
                }
                else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
                break;
            default:
                httpExchange.sendResponseHeaders(400, 0);
                break;
        }
        httpExchange.close();
    }
    private static void handleProducts(HttpExchange httpExchange, boolean safe) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch (method) {
            case "GET":
                Utils.sendFile(httpExchange, new File("articoli.html"), "text/html; charset=utf-8");
                break;
            case "POST":
                HashMap<String, String> postData = Utils.parsePost(httpExchange);
                if (postData.containsKey("category")) {
                    String category = postData.get("category");
                    try {
                        String json = (safe ? Critical.recordRetrievalFixed(category) : Critical.recordRetrievalVuln(category));
                        Utils.sendJson(httpExchange, json);
                    } catch (Exception e) {
                        System.err.println("Eccezione: " + e.getMessage());
                    }
                }
                else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
                break;
            default:
                httpExchange.sendResponseHeaders(400, 0);
                break;
        }
        httpExchange.close();
    }
}
