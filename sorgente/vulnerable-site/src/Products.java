import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.util.HashMap;

public class Products {
    public static HttpHandler vulnerableSiteHandler = (httpExchange) -> {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                String queryString = httpExchange.getRequestURI().getQuery();
                Utils.sendFile(httpExchange, new File("articoli.html"), "text/html; charset=utf-8");
                break;
            case "POST":
                HashMap<String, String> postData = Utils.parsePost(httpExchange);
                postData.forEach((k, v) -> {
                    System.out.println(k + " " + v);
                });
                if (postData.containsKey("category")) {
                    String category = postData.get("category");
                    try {
                        Utils.sendJson(httpExchange, Critical.recordRetrievalVuln(category));
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
