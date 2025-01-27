package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Stellt die Verbindung zur Datenbank bereit.
 */
public class DbManager {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/projektDB";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    /**
     * Erstellt eine Verbindung zur Datenbank.
     *
     * @return Eine Connection zur Datenbank.
     * @throws SQLException Falls die Verbindung fehlschlägt.
     */
    public static Connection connect() throws SQLException {
        try {
            System.out.println("Verbindung mit Datenbank hergestellt.");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Verbindung zur Datenbank fehlgeschlagen. Bitte prüfen Sie die Konfiguration!", e);
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connection to database closed");
                }
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
            }
        }
    }
}



    /*
    public static void main(String[] args) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

*/