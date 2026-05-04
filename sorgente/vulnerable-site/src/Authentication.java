import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Properties;

public record Authentication(boolean ok, String executedQuery) {
    public String toJson() {
        return "{ \"ok\": " + this.ok + ", \"executedQuery\": \"" + this.executedQuery + "\" }";
    }
}
