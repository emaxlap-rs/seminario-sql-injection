import java.sql.*;
import java.util.Properties;

public class Authentication {
    private static Connection dbConnection;
    public static void init() throws SQLException {
        String url = "jdbc:postgresql://localhost/example";
        Properties props = new Properties();
        props.setProperty("user", "example");
        dbConnection = DriverManager.getConnection(url, props);
    }

    // La parte importante è qui!
    public static Authentication authVuln(String username, String password) throws SQLException {
        Statement stmt = dbConnection.createStatement();

        // È qui che avviene il danno, la query è concatenata direttamente ai parametri attuali del metodo
        String query = "SELECT * " +
                "FROM login.users " +
                "WHERE users.username = '" + username + "' AND users.password = '" + password +"'";
        ResultSet rs = stmt.executeQuery(query);

        // La prima chiamata del metodo 'next()' restituisce true se è presente almeno una riga nella tabella risultato
        // in questo caso estremamente semplificato concediamo l'accesso se l'utente esiste nella tabella 'users' e
        // il valore di password coincide con quello presente nella tupla dell'utente.
        // Il valore 'false' è considerato come un'autenticazione fallita.
        return new Authentication(rs.next(), stmt.toString());
    }

    public static Authentication authFixed(String username, String password) throws SQLException {
        // In questo caso la query si dice 'preparata' perché non contiene i valori in partenza bensì viene
        // popolata successivamente tramite il metodo 'setString()' dove i '?' forniscono un segnaposto
        String query = "SELECT * " +
                "FROM login.users " +
                "WHERE users.username = ? AND users.password = ?";

        PreparedStatement stmt = dbConnection.prepareStatement(query);

        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        // rs.next() chiamata ora restituisce true se è presente una riga nella tabella risultato
        return new Authentication(rs.next(), stmt.toString());
    }

    // #################################################

    private final boolean ok;
    private final String executedQuery;
    public Authentication(boolean authenticated, String executedQuery) {
        this.ok = authenticated;
        this.executedQuery = executedQuery;
    }
    public boolean isOk() {
        return this.ok;
    }
    public String getExecutedQuery() {
        return this.executedQuery;
    }
    public String toJson() {
        return "{ \"ok\": " + this.ok + ", \"executedQuery\": \"" + this.executedQuery + "\" }";
    }
}
