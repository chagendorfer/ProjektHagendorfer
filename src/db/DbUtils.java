package db;

import model.Patient;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class DbUtils {
    public static void setPatientParameters(PreparedStatement stmt, Patient patient) throws SQLException {
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
    }

    public static Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt("patientID"),
                rs.getString("vorname"),
                rs.getString("nachname"),
                rs.getString("anrede"),
                rs.getDate("geburtsdatum").toLocalDate(),
                rs.getString("strasse"),
                rs.getString("plz"),
                rs.getString("ort"),
                rs.getInt("bundeslandID"),
                rs.getString("telefon"),
                rs.getInt("geschlechtID"),
                rs.getInt("krankenkasseID"),
                rs.getString("sonstiges")
        );
    }
}
