package DBComponent;

public class DemoApp extends Thread {
    private final DBComponent dbComponent;

    public DemoApp(DBComponent dbComponent) {
        this.dbComponent = dbComponent;
    }

    @Override
    public void run() {
        // Ejecutar consultas
        dbComponent.executeQuery("SELECT * FROM tabla");
        dbComponent.executeQuery("INSERT INTO tabla (columna1, columna2) VALUES ('valor1', 'valor2')");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            // Creación de un thread para cada conexión
            new DemoApp(new DBComponent(new ConnectionPool(5))).start();
        }
    }

}



