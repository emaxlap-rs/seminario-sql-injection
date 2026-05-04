import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost/example";
    private static final String DATABASE_USER = "example";

    private static Connection connection = null;
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            Properties props = new Properties();
            props.setProperty("user", DATABASE_USER);
            connection = DriverManager.getConnection(DATABASE_URL, props);
        }
        return connection;
    }
}
