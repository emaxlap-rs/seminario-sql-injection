import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Critical {
    public static boolean authVuln(String username, String password) throws SQLException {
        Statement stmt = Database.getConnection().createStatement();

        // È qui che avviene il danno, la query è concatenata direttamente ai parametri attuali del metodo
        String query = "SELECT * " +
                "FROM login.users " +
                "WHERE users.username = '" + username + "' AND users.password = '" + password +"'";

        System.out.println("Executed query: " + query);

        ResultSet rs = stmt.executeQuery(query);

        // La prima chiamata del metodo 'next()' restituisce true se è presente almeno una riga nella tabella risultato
        // in questo caso estremamente semplificato concediamo l'accesso se l'utente esiste nella tabella 'users' e
        // il valore di password coincide con quello presente nella tupla dell'utente.
        // Il valore 'false' è considerato come un'autenticazione fallita.
        return rs.next();
    }

    public static boolean authFixed(String username, String password) throws SQLException {
        // In questo caso la query si dice 'preparata' perché non contiene i valori in partenza bensì viene
        // popolata successivamente tramite il metodo 'setString()' dove i '?' forniscono un segnaposto
        String query = "SELECT * " +
                "FROM login.users " +
                "WHERE users.username = ? AND users.password = ?";

        PreparedStatement stmt = Database.getConnection().prepareStatement(query);

        stmt.setString(1, username);
        stmt.setString(2, password);

        System.out.println("Executed query: " + stmt.toString());

        ResultSet rs = stmt.executeQuery();

        // rs.next() chiamata ora restituisce true se è presente una riga nella tabella risultato
        return rs.next();
    }

    public static String recordRetrievalVuln(String category) throws SQLException {
        String query;
        if (!category.isEmpty()) {
            query = "SELECT * FROM products.products WHERE category = '" + category + "'";
        }
        else {
            query = "SELECT * FROM products.products";
        }

        Statement stmt = Database.getConnection().createStatement();

        System.out.println("Executed query: " + query);

        ResultSet rs = stmt.executeQuery(query);

        return Utils.resultSetToJson(rs);
    }

    public static String recordRetrievalFixed(String category) throws SQLException {
        String query;
        if (!category.isEmpty()) {
            query = "SELECT * FROM products.products WHERE category = ?";
        }
        else {
            query = "SELECT * FROM products.products";
        }

        PreparedStatement stmt = Database.getConnection().prepareStatement(query);

        if (!category.isEmpty()) {
            stmt.setString(1, category);
        }

        System.out.println("Executed query: " + stmt.toString());

        ResultSet rs = stmt.executeQuery();

        return Utils.resultSetToJson(rs);
    }
}
