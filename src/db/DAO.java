package db;

import model.Patient;
import utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse DAO (Data Access Object) dient als zentrale Schnittstelle für Datenbankoperationen.
 * Sie kapselt CRUD-Operationen (Create, Read, Update, Delete) für die Patientenverwaltung und stellt sicher,
 * dass die Datenbankinteraktionen abstrahiert und organisiert sind.
 */
public class DAO {

    private static final String INSERT_SQL =
            """
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

    private static final String SELECT_ALL_SQL = "SELECT * FROM Patient";

    private static final String UPDATE_SQL = """
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

    private static final String DELETE_SQL = "DELETE FROM Patient WHERE PatientID = ?";


    /**
     * Fügt einen neuen Patienten in die Datenbank ein.
     *
     * @param patient Das Patient-Objekt mit den einzufügenden Daten.
     * @throws SQLException, wenn ein Fehler beim Zugriff auf die Datenbank auftritt.
     * @throws IllegalArgumentException, wenn ungültige Daten eingegeben werden.
     */
    public void addPatient(Patient patient) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_SQL)) {
            Helper.setPatientParameters(stmt, patient);
            stmt.executeUpdate();
            Logger.log(Logger.LogLevel.INFO, "Patient hinzugefügt: " + patient);
        } catch (SQLIntegrityConstraintViolationException e) {
            Logger.log(Logger.LogLevel.WARN, "Ungültige Eingabe für Patient: " + patient, e);
            throw new IllegalArgumentException("Ungültige Eingabe. Überprüfen Sie die IDs oder doppelte Daten!", e);
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Hinzufügen eines Patienten: " + patient, e);
            throw e;
        }
    }


    /**
     * Gibt alle Patienten aus der Datenbank zurück.
     *
     * @return Eine Liste von Patienten.
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patients.add(Helper.mapResultSetToPatient(rs, null, connection));
            }
            Logger.log(Logger.LogLevel.INFO, "Alle Patienten abgerufen. Anzahl: " + patients.size());
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Abrufen der Patientenliste.", e);
        }
        return patients;
    }

    /**
     * Aktualisiert die Daten eines bestehenden Patienten.
     *
     * @param patient Das aktualisierte Patient-Objekt.
     * @throws SQLException Wenn ein Fehler beim Zugriff auf die Datenbank auftritt.
     */
    public void updatePatient(Patient patient) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            Helper.setPatientParameters(stmt, patient);
            stmt.setInt(13, patient.getPatientID());
            int updatedRows = stmt.executeUpdate();
            if (updatedRows > 0) {
                Logger.log(Logger.LogLevel.INFO, "Patient aktualisiert: " + patient);
            } else {
                Logger.log(Logger.LogLevel.WARN, "Kein Patient aktualisiert. Möglicherweise ungültige ID: " + patient.getPatientID());
            }
        }
    }

    /**
     * Löscht einen Patienten anhand seiner ID.
     *
     * @param id Die ID des Patienten.
     * @throws SQLException Wenn ein Fehler beim Zugriff auf die Datenbank auftritt.
     */
    public void deletePatient(int id) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            int deletedRows = stmt.executeUpdate();
            if (deletedRows > 0) {
                Logger.log(Logger.LogLevel.INFO, "Patient gelöscht: ID=" + id);
            } else {
                Logger.log(Logger.LogLevel.WARN, "Kein Patient gelöscht. Möglicherweise ungültige ID: " + id);
            }
        }
    }
}
