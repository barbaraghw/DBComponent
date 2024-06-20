package DBComponent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {
    private final int poolSize;
    private final BlockingQueue<Connection> connections;
    private final Thread connectionRefillThread;

    public ConnectionPool(int poolSize) {
        this.poolSize = poolSize;
        this.connections = new LinkedBlockingQueue<>(poolSize);

        // Iniciar el hilo para rellenar el pool de conexiones
        connectionRefillThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (connections.size() < poolSize) {
                        connections.add(DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "your_username", "your_password"));
                    }
                    Thread.sleep(1000); // Esperar 1 segundo antes de verificar de nuevo
                } catch (SQLException | InterruptedException e) {
                    // Manejar excepciones
                    Thread.currentThread().interrupt();
                }
            }
        });
        connectionRefillThread.start();
    }

    public Connection getConnection() throws InterruptedException {
        return connections.poll(5, TimeUnit.SECONDS);
    }

    public void returnConnection(Connection connection) {
        connections.add(connection);
    }

    public void shutdown() {
        connectionRefillThread.interrupt();
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Manejar excepciones
            }
        }
    }
}

