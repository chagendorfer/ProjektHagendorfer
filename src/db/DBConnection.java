package db;

import utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Stellt die Verbindung zur Datenbank bereit.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/projektDB?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    /**
     * Erstellt eine Verbindung zur Datenbank.
     *
     * @return Eine Connection zur Datenbank.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Logger.log(Logger.LogLevel.INFO, "Verbindung zur Datenbank wird hergestellt.");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Schließt die Verbindung zur Datenbank, falls sie geöffnet ist.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    Logger.log(Logger.LogLevel.INFO, "Datenbankverbindung geschlossen.");
                }
            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.ERROR, "Fehler beim Schließen der Datenbankverbindung.", e);
            }
        }
    }
}
