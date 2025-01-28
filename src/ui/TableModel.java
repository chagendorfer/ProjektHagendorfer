package ui;

import model.Patient;
import utils.Logger;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "ID", "Vorname", "Nachname", "Anrede", "Geburtsdatum",
            "Straße", "PLZ", "Ort", "BundeslandID",
            "Telefon", "GeschlechtID", "KrankenkasseID", "Sonstiges"
    };

    private List<Patient> patients = new ArrayList<>();

    @Override
    public int getRowCount() {
        Logger.log(Logger.LogLevel.DEBUG, "Anzahl der Zeilen abgefragt: " + patients.size());
        return patients.size();
    }

    @Override
    public int getColumnCount() {
        Logger.log(Logger.LogLevel.DEBUG, "Anzahl der Spalten abgefragt: " + columnNames.length);
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Patient p = patients.get(rowIndex);
            Logger.log(Logger.LogLevel.DEBUG, "Wert abgefragt für Zeile " + rowIndex + ", Spalte " + columnIndex);
            return switch (columnIndex) {
                case 0 -> p.getPatientID();
                case 1 -> p.getVorname();
                case 2 -> p.getNachname();
                case 3 -> p.getAnrede();
                case 4 -> p.getGeburtsdatum();   // LocalDate
                case 5 -> p.getStrasse();
                case 6 -> p.getPlz();
                case 7 -> p.getOrt();
                case 8 -> p.getBundeslandName();
                case 9 -> p.getTelefon();
                case 10 -> p.getGeschlechtName();
                case 11 -> p.getKrankenkasseName();
                case 12 -> p.getSonstiges();
                default -> {
                    Logger.log(Logger.LogLevel.WARN, "Ungültiger Spaltenindex: " + columnIndex);
                    yield "";
                }
            };
        } catch (IndexOutOfBoundsException e) {
            Logger.log(Logger.LogLevel.ERROR, "Ungültiger Zeilenindex: " + rowIndex, e);
            return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < columnNames.length) {
            return columnNames[column];
        } else {
            Logger.log(Logger.LogLevel.WARN, "Ungültiger Spaltenindex: " + column);
            return "";
        }
    }

    public void setPatients(List<Patient> patients) {
        Logger.log(Logger.LogLevel.INFO, "Patientenliste aktualisiert. Neue Anzahl: " + patients.size());
        this.patients = patients;
        fireTableDataChanged();
    }

    public Patient getPatientAt(int rowIndex) {
        try {
            Logger.log(Logger.LogLevel.DEBUG, "Patient abgefragt für Zeile: " + rowIndex);
            return patients.get(rowIndex);
        } catch (IndexOutOfBoundsException e) {
            Logger.log(Logger.LogLevel.ERROR, "Ungültiger Zeilenindex: " + rowIndex, e);
            return null;
        }
    }
}
