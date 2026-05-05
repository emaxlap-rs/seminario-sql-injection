import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.util.HashMap;

public class Login {
    public static HttpHandler vulnerableSiteHandler = (httpExchange) -> {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                String queryString = httpExchange.getRequestURI().getQuery();
                Utils.sendFile(httpExchange, new File("login.html"), "text/html; charset=utf-8");
                break;
            case "POST":
                HashMap<String, String> postData = Utils.parsePost(httpExchange);
                postData.forEach((k, v) -> {
                    System.out.println(k + " " + v);
                });
                if (postData.containsKey("username") &&  postData.containsKey("password")) {
                    String username = postData.get("username");
                    String password = postData.get("password");
                    try {
                        String ans = "{ \"ok\": " + Critical.authVuln(username, password) + " }";
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
    };

    public static HttpHandler fixedSiteHandler = (httpExchange) -> {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                String queryString = httpExchange.getRequestURI().getQuery();
                Utils.sendFile(httpExchange, new File("login.html"), "text/html; charset=utf-8");
                break;
            case "POST":
                HashMap<String, String> postData = Utils.parsePost(httpExchange);
                if (postData.containsKey("username") &&  postData.containsKey("password")) {
                    String username = postData.get("username");
                    String password = postData.get("password");
                    try {
                        String ans = "{ \"ok\": " + Critical.authFixed(username, password) + " }";
                        Utils.sendJson(httpExchange, ans);
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
    };
}
