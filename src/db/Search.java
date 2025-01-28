package db;

import model.Patient;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Diese Klasse ermöglicht die Suche nach Patienten anhand dynamischer Kriterien.
 */
public class Search {

    /**
     * Sucht Patienten in der Datenbank basierend auf den übergebenen Kriterien.
     *
     * @param criteria Eine Map, die Spaltennamen (z. B. "Vorname") den entsprechenden Suchwerten zuordnet.
     * @return Eine Liste der gefundenen Patienten.
     * @throws SQLException Falls ein Fehler bei der Datenbankabfrage auftritt.
     */
    public static List<Patient> searchPatients(Map<String, String> criteria) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Patient WHERE 1=1");

        // Dynamisches SQL-Statement basierend auf den Kriterien aufbauen
        criteria.forEach((column, value) -> {
            if (value != null && !value.isEmpty()) {
                sql.append(" AND ").append(column).append(" LIKE ?");
            }
        });

        Logger.log(Logger.LogLevel.DEBUG, "Auszuführendes SQL-Statement: " + sql);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            // Parameter binden
            int paramIndex = 1;
            for (String value : criteria.values()) {
                if (value != null && !value.isEmpty()) {
                    stmt.setString(paramIndex++, "%" + value.trim() + "%");
                }
            }

            // Abfrage ausführen und Ergebnisse verarbeiten
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(Helper.mapResultSetToPatient(rs, null, connection));
                }
            }

            Logger.log(Logger.LogLevel.INFO, "Gefundene Patienten: " + patients.size());
        }

        return patients;
    }
}
