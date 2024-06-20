package DBComponent;

import java.sql.Connection;
import java.sql.SQLException;

public class DBComponent {
    private final ConnectionPool connectionPool;

    public DBComponent(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void executeQuery(String query) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.createStatement().execute(query);
        } catch (SQLException | InterruptedException e) {
            // Manejar excepciones
        }
    }
}



