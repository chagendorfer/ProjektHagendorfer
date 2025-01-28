package db;

import model.Patient;
import utils.Logger;

import java.sql.*;

public class Helper {

    /**
     * Setzt die Parameter eines PreparedStatements basierend auf einem Patient-Objekt.
     *
     * @param stmt    Das PreparedStatement.
     * @param patient Das Patient-Objekt.
     * @throws SQLException Falls ein SQL-Fehler auftritt.
     */
    public static PreparedStatement setPatientParameters(PreparedStatement stmt, Patient patient) throws SQLException {
        try {
            stmt.setString(1, patient.getVorname());
            stmt.setString(2, patient.getNachname());
            stmt.setString(3, patient.getAnrede());
            stmt.setDate(4, patient.getGeburtsdatum() != null ? java.sql.Date.valueOf(patient.getGeburtsdatum()) : null);
            stmt.setString(5, patient.getStrasse());
            stmt.setString(6, patient.getPlz());
            stmt.setString(7, patient.getOrt());
            stmt.setInt(8, patient.getBundeslandID());
            stmt.setString(9, patient.getTelefon());
            stmt.setInt(10, patient.getGeschlechtID());
            stmt.setInt(11, patient.getKrankenkasseID());
            stmt.setString(12, patient.getSonstiges());
            Logger.log(Logger.LogLevel.DEBUG, "SQL-Parameter für Patient gesetzt: " + patient);
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Setzen der SQL-Parameter für Patient.", e);
            throw e;
        }
        return stmt;
    }

    /**
     * Mappt ein ResultSet auf ein Patient-Objekt.
     *
     * @param rs            Das ResultSet, das Daten aus der Datenbank enthält.
     * @param patient       Ein existierendes Patient-Objekt oder null.
     * @param connection    Die aktive Datenbankverbindung.
     * @return Ein Patient-Objekt, das mit den Daten aus dem ResultSet befüllt ist.
     * @throws SQLException Wenn ein Fehler beim Lesen der Daten auftritt.
     */
    public static Patient mapResultSetToPatient(ResultSet rs, Patient patient, Connection connection) throws SQLException {
        try {
            Logger.log(Logger.LogLevel.DEBUG, "Patient wird aus ResultSet gemappt.");
            Logger.log(Logger.LogLevel.DEBUG, "Eingehendes Patient-Objekt: " + (patient == null ? "null" : patient.toString()));
            // Falls kein bestehendes Patient-Objekt übergeben wird, ein neues erstellen
            if (patient == null) {
                patient = new Patient();
                Logger.log(Logger.LogLevel.WARN, "Es wird ein neuer Patient erstellt.");
            }
            if (patient.getPatientID() != 0) {
                Logger.log(Logger.LogLevel.WARN, "Ein bereits initialisierter Patient wird überschrieben. Überprüfen Sie den Aufruf!");
            }

            // Werte setzen nur, wenn sie aktuell leer sind
            if (patient.getPatientID() == 0) patient.setPatientID(rs.getInt("patientID"));
            if (patient.getVorname() == null) patient.setVorname(rs.getString("vorname"));
            if (patient.getNachname() == null) patient.setNachname(rs.getString("nachname"));
            if (patient.getAnrede() == null) patient.setAnrede(rs.getString("anrede"));
            if (patient.getGeburtsdatum() == null && rs.getDate("geburtsdatum") != null) {
                patient.setGeburtsdatum(rs.getDate("geburtsdatum").toLocalDate());
            }
            if (patient.getStrasse() == null) patient.setStrasse(rs.getString("strasse"));
            if (patient.getPlz() == null) patient.setPlz(rs.getString("plz"));
            if (patient.getOrt() == null) patient.setOrt(rs.getString("ort"));
            if (patient.getBundeslandID() == 0) patient.setBundeslandID(rs.getInt("bundeslandID"));
            if (patient.getTelefon() == null) patient.setTelefon(rs.getString("telefon"));
            if (patient.getGeschlechtID() == 0) patient.setGeschlechtID(rs.getInt("geschlechtID"));
            if (patient.getKrankenkasseID() == 0) patient.setKrankenkasseID(rs.getInt("krankenkasseID"));
            if (patient.getSonstiges() == null) patient.setSonstiges(rs.getString("sonstiges"));

            // IDs in Namen übersetzen (nur falls noch nicht gesetzt)
            if (connection != null) {
                if (patient.getBundeslandName() == null) {
                    patient.setBundeslandName(getBundeslandName(connection, patient.getBundeslandID()));
                }
                if (patient.getGeschlechtName() == null) {
                    patient.setGeschlechtName(getGeschlechtName(connection, patient.getGeschlechtID()));
                }
                if (patient.getKrankenkasseName() == null) {
                    patient.setKrankenkasseName(getKrankenkasseName(connection, patient.getKrankenkasseID()));
                }
            }

            Logger.log(Logger.LogLevel.DEBUG, "Patient erfolgreich gemappt: " + patient);
            return patient;
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Mappen des ResultSet zu Patient.", e);
            throw e;
        }
    }

    /**
     * Gibt den Namen eines Bundeslandes basierend auf seiner ID zurück.
     *
     * @param connection    Die aktive Datenbankverbindung.
     * @param bundeslandID  Die ID des Bundeslandes.
     * @return Der Name des Bundeslandes.
     * @throws SQLException Wenn ein Fehler beim Abrufen des Namens auftritt.
     */
    public static String getBundeslandName(Connection connection, int bundeslandID) throws SQLException {
        String sql = "SELECT Bezeichnung FROM bundesland WHERE BundeslandID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bundeslandID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Bezeichnung");
                }
            }
        }
        return "Unbekannt";
    }

    /**
     * Gibt den Namen eines Geschlechts basierend auf seiner ID zurück.
     *
     * @param connection   Die aktive Datenbankverbindung.
     * @param geschlechtID Die ID des Geschlechts.
     * @return Der Name des Geschlechts.
     * @throws SQLException Wenn ein Fehler beim Abrufen des Namens auftritt.
     */
    public static String getGeschlechtName(Connection connection, int geschlechtID) throws SQLException {
        String sql = "SELECT Bezeichnung FROM geschlecht WHERE GeschlechtID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, geschlechtID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Bezeichnung");
                }
            }
        }
        return "Unbekannt";
    }

    /**
     * Gibt den Namen einer Krankenkasse basierend auf ihrer ID zurück.
     *
     * @param connection       Die aktive Datenbankverbindung.
     * @param krankenkasseID   Die ID der Krankenkasse.
     * @return Der Name der Krankenkasse.
     * @throws SQLException Wenn ein Fehler beim Abrufen des Namens auftritt.
     */
    public static String getKrankenkasseName(Connection connection, int krankenkasseID) throws SQLException {
        String sql = "SELECT Bezeichnung FROM krankenkasse WHERE KrankenkasseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, krankenkasseID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Bezeichnung");
                }
            }
        }
        return "Unbekannt";
    }
}