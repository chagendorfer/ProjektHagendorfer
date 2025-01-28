package db;

import model.Patient;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class Helper {
    public static void setPatientParameters(PreparedStatement stmt, Patient patient) throws SQLException {
        try {
            stmt.setString(1, patient.getVorname());
            stmt.setString(2, patient.getNachname());
            stmt.setString(3, patient.getAnrede());
            stmt.setDate(4, java.sql.Date.valueOf(patient.getGeburtsdatum()));
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
    }

    public static Patient mapResultSetToPatient(ResultSet rs, Connection con) throws SQLException {
        try {
            Logger.log(Logger.LogLevel.DEBUG, "Patient wird aus ResultSet gemappt.");
            Patient patient = new Patient();
            patient.setPatientID(rs.getInt("patientID"));
            patient.setVorname(rs.getString("vorname"));
            patient.setNachname(rs.getString("nachname"));
            patient.setAnrede(rs.getString("anrede"));
            patient.setGeburtsdatum(rs.getDate("geburtsdatum").toLocalDate());
            patient.setStrasse(rs.getString("strasse"));
            patient.setPlz(rs.getString("plz"));
            patient.setOrt(rs.getString("ort"));
            patient.setBundeslandID(rs.getInt("bundeslandID"));
            patient.setTelefon(rs.getString("telefon"));
            patient.setGeschlechtID(rs.getInt("geschlechtID"));
            patient.setKrankenkasseID(rs.getInt("krankenkasseID"));
            patient.setSonstiges(rs.getString("sonstiges"));

            // IDs in Namen übersetzen
            patient.setBundeslandName(getBundeslandName(con, patient.getBundeslandID()));
            patient.setGeschlechtName(getGeschlechtName(con, patient.getGeschlechtID()));
            patient.setKrankenkasseName(getKrankenkasseName(con, patient.getKrankenkasseID()));

            Logger.log(Logger.LogLevel.DEBUG, "Patient erfolgreich gemappt: " + patient);
            return patient;
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Mappen des ResultSet zu Patient.", e);
            throw e;
        }
    }
    public static String getBundeslandName(Connection con, int bundeslandID) throws SQLException {
        String sql = "SELECT Bezeichnung FROM bundesland WHERE BundeslandID = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, bundeslandID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("Bezeichnung");
                    Logger.log(Logger.LogLevel.DEBUG, "Bundesland-Name gefunden: " + name);
                    return name;
                }
            }
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Abrufen des Bundesland-Namens für ID: " + bundeslandID, e);
            throw e;
        }
        Logger.log(Logger.LogLevel.WARN, "Bundesland-ID nicht gefunden: " + bundeslandID);
        return "Unbekannt";
    }

    public static String getGeschlechtName(Connection con, int geschlechtID) throws SQLException {
        String sql = "SELECT Bezeichnung FROM geschlecht WHERE GeschlechtID = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, geschlechtID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("Bezeichnung");
                    Logger.log(Logger.LogLevel.DEBUG, "Geschlecht-Name gefunden: " + name);
                    return name;
                }
            }
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Abrufen des Geschlecht-Namens für ID: " + geschlechtID, e);
            throw e;
        }
        Logger.log(Logger.LogLevel.WARN, "Geschlecht-ID nicht gefunden: " + geschlechtID);
        return "Unbekannt";
    }

    public static String getKrankenkasseName(Connection con, int krankenkasseID) throws SQLException {
        String sql = "SELECT Bezeichnung FROM krankenkasse WHERE KrankenkasseID = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, krankenkasseID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("Bezeichnung");
                    Logger.log(Logger.LogLevel.DEBUG, "Krankenkasse-Name gefunden: " + name);
                    return name;
                }
            }
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Abrufen des Krankenkasse-Namens für ID: " + krankenkasseID, e);
            throw e;
        }
        Logger.log(Logger.LogLevel.WARN, "Krankenkasse-ID nicht gefunden: " + krankenkasseID);
        return "Unbekannt";
    }
}