package db;

import model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PatientDAO implements AutoCloseable {

    private Connection connection; // Bleibt offen, bis close() aufgerufen wird

    public PatientDAO() throws SQLException {
        // Verbindung EINMAL beim Anlegen der DAO öffnen
        this.connection = DbManager.connect();
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            DbUtils.setPatientParameters(stmt, patient); // Hilfsmethode
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // Ungültige Fremdschlüssel oder doppelte Einträge
            throw new IllegalArgumentException("Ungültige Eingabe: Überprüfen Sie die IDs oder doppelte Daten!", e);
        } catch (SQLException e) {
            // Alle anderen SQL-bezogenen Fehler
            throw new RuntimeException("Datenbankfehler beim Hinzufügen des Patienten.", e);
        }
    }


    public Patient getPatientByID(int id) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return DbUtils.mapResultSetToPatient(rs); // Hilfsmethode
            }
        }
        return null;
    }


    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                patients.add(DbUtils.mapResultSetToPatient(rs));
            }
        }
        return patients;
    }


    public void updatePatient(Patient patient) throws SQLException {
        String sql = """
    UPDATE Patient
    SET vorname = ?,
        nachname = ?,
        anrede = ?,
        geburtsdatum = ?,
        strasse = ?,
        plz = ?,
        ort = ?,
        bundeslandID = ?,
        telefon = ?,
        geschlechtID = ?,
        krankenkasseID = ?,
        sonstiges = ?
    WHERE id = ?
    """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            DbUtils.setPatientParameters(stmt, patient);
            stmt.setInt(13, patient.getPatientID());
            stmt.executeUpdate();
        }
    }

    public void deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM Patient WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Verbindung mit Datenbank wurde geschlossen.");
        }
    }
}
