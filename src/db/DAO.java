package db;

import model.Patient;
import utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DAO {

    private static DAO instance;
    private final Connection connection; // Bleibt offen, bis close() aufgerufen wird

    private DAO() {
        // Verbindung EINMAL beim Anlegen der DAO öffnen
        this.connection = DBConnection.connect();
        Logger.log(Logger.LogLevel.INFO, "DAO erstellt und Datenbankverbindung geöffnet.");
    }

    public static synchronized DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    public void addPatient(Patient patient) throws SQLException {
        String sql = """
        INSERT INTO Patient (
            vorname,
            nachname,
            anrede,
            geburtsdatum,
            strasse,
            plz,
            ort,
            bundeslandID,
            telefon,
            geschlechtID,
            krankenkasseID,
            sonstiges
        )
        VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
        """;
        Logger.log(Logger.LogLevel.DEBUG, "SQL-Statement wird ausgeführt: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            db.Helper.setPatientParameters(stmt, patient); // Hilfsmethode
            stmt.executeUpdate();
            Logger.log(Logger.LogLevel.INFO, "Patient hinzugefügt: " + patient);
        } catch (SQLIntegrityConstraintViolationException e) {
            Logger.log(Logger.LogLevel.WARN, "Ungültige Eingabe für Patient: " + patient, e);
            throw new IllegalArgumentException("Ungültige Eingabe: Überprüfen Sie die IDs oder doppelte Daten!", e);
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Hinzufügen eines Patienten: " + patient, e);
            throw new RuntimeException("Datenbankfehler beim Hinzufügen des Patienten.", e);
        }
    }


    public Patient getPatientByID(int id) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE PatientID = ?";
        Logger.log(Logger.LogLevel.DEBUG, "SQL-Statement wird ausgeführt: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Logger.log(Logger.LogLevel.INFO, "Patient abgerufen: ID=" + id);
                return db.Helper.mapResultSetToPatient(rs, connection); // Hilfsmethode
            } else {
                Logger.log(Logger.LogLevel.WARN, "Kein Patient gefunden mit ID=" + id);
            }
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Abrufen eines Patienten mit ID=" + id, e);
            throw e;
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient";
        Logger.log(Logger.LogLevel.DEBUG, "SQL-Statement wird ausgeführt: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient patient = db.Helper.mapResultSetToPatient(rs, connection);
                patients.add(patient);
            }
            Logger.log(Logger.LogLevel.INFO, "Alle Patienten abgerufen. Anzahl: " + patients.size());
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Abrufen der Patientenliste.", e);
        }
        return patients;
    }
    public void updatePatient(Patient patient) throws SQLException {
        String sql = """
        UPDATE Patient
        SET Vorname = ?,
            Nachname = ?,
            Anrede = ?,
            Geburtsdatum = ?,
            Strasse = ?,
            PLZ = ?,
            Ort = ?,
            BundeslandID = ?,
            Telefon = ?,
            GeschlechtID = ?,
            KrankenkasseID = ?,
            Sonstiges = ?
        WHERE PatientID = ?
        """;
        Logger.log(Logger.LogLevel.DEBUG, "SQL-Statement wird ausgeführt: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            db.Helper.setPatientParameters(stmt, patient);
            stmt.setInt(13, patient.getPatientID());
            int updatedRows = stmt.executeUpdate();
            if (updatedRows > 0) {
                Logger.log(Logger.LogLevel.INFO, "Patient aktualisiert: " + patient);
            } else {
                Logger.log(Logger.LogLevel.WARN, "Kein Patient aktualisiert. Möglicherweise ungültige ID: " + patient.getPatientID());
            }
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Aktualisieren eines Patienten: " + patient, e);
            throw e;
        }
    }

    public void deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM Patient WHERE PatientID = ?";
        Logger.log(Logger.LogLevel.DEBUG, "SQL-Statement wird ausgeführt: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deletedRows = stmt.executeUpdate();
            if (deletedRows > 0) {
                Logger.log(Logger.LogLevel.INFO, "Patient gelöscht: ID=" + id);
            } else {
                Logger.log(Logger.LogLevel.WARN, "Kein Patient gelöscht. Möglicherweise ungültige ID: " + id);
            }
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Löschen eines Patienten mit ID=" + id, e);
            throw e;
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            Logger.log(Logger.LogLevel.DEBUG, "Versuche, die Verbindung zu schließen.");
            connection.close();
            Logger.log(Logger.LogLevel.INFO, "Datenbankverbindung geschlossen.");
        } else {
            Logger.log(Logger.LogLevel.WARN, "Verbindung war bereits geschlossen oder null.");
        }
    }
}