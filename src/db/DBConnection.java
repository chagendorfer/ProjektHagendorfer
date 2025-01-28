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

    /**
     * Erstellt eine Verbindung zur Datenbank.
     *
     * @return Eine Connection zur Datenbank.
     */
    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Logger.log(Logger.LogLevel.INFO, "Verbindung zur Datenbank hergestellt.");
            return connection;
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Aufbau der Datenbankverbindung.", e);
            throw new RuntimeException("Datenbankverbindung fehlgeschlagen", e);
        }
    }
}
