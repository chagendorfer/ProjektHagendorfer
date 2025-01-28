package ui;

import model.Patient;
import utils.Logger;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Eine Implementierung von {@link AbstractTableModel}, die als Datenquelle für eine Tabelle dient,
 * die Patientendaten anzeigt.
 * <p>
 * Die Spalten enthalten Informationen wie Name, Adresse, Geburtsdatum und andere relevante Patientendaten.
 * Änderungen an der Patientenliste werden automatisch in der Tabelle angezeigt.
 */
public class TableModel extends AbstractTableModel {

    /** Die Namen der Spalten in der Tabelle. */
    private final String[] columnNames = {
            "ID", "Vorname", "Nachname", "Anrede", "Geburtsdatum",
            "Straße", "PLZ", "Ort", "BundeslandID",
            "Telefon", "GeschlechtID", "KrankenkasseID", "Sonstiges"
    };

    /** Die Liste der Patienten, die in der Tabelle angezeigt werden. */
    private List<Patient> patients = new ArrayList<>();

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Die Anzahl der Patienten in der Liste.
     */
    @Override
    public int getRowCount() {
        return patients.size();
    }

    /**
     * Gibt die Anzahl der Spalten in der Tabelle zurück.
     *
     * @return Die Anzahl der Spalten, die durch {@code columnNames} definiert sind.
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Gibt den Wert an einer bestimmten Zeile und Spalte zurück.
     *
     * @param rowIndex    Der Index der Zeile (0-basiert).
     * @param columnIndex Der Index der Spalte (0-basiert).
     * @return Der Wert an der angegebenen Position oder {@code null}, wenn der Index ungültig ist.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Patient p = patients.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> p.getPatientID();
                case 1 -> p.getVorname();
                case 2 -> p.getNachname();
                case 3 -> p.getAnrede();
                case 4 -> p.getGeburtsdatum(); // LocalDate
                case 5 -> p.getStrasse();
                case 6 -> p.getPlz();
                case 7 -> p.getOrt();
                case 8 -> p.getBundeslandName();
                case 9 -> p.getTelefon();
                case 10 -> p.getGeschlechtName();
                case 11 -> p.getKrankenkasseName();
                case 12 -> p.getSonstiges();
                default -> "";
            };
        } catch (IndexOutOfBoundsException e) {
            Logger.log(Logger.LogLevel.ERROR, "Ungültiger Zeilenindex: " + rowIndex, e);
            return null;
        }
    }

    /**
     * Gibt den Namen einer bestimmten Spalte zurück.
     *
     * @param column Der Index der Spalte (0-basiert).
     * @return Der Name der Spalte oder ein leerer String, wenn der Index ungültig ist.
     */
    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < columnNames.length) {
            return columnNames[column];
        } else {
            Logger.log(Logger.LogLevel.WARN, "Ungültiger Spaltenindex: " + column);
            return "";
        }
    }

    /**
     * Aktualisiert die Liste der Patienten und informiert die Tabelle über die Änderungen.
     *
     * @param newPatients Die neue Liste der Patienten. Falls {@code null}, wird keine Aktion ausgeführt.
     */
    public void setPatients(List<Patient> newPatients) {
        if (newPatients == null) {
            Logger.log(Logger.LogLevel.WARN, "Patientenliste ist null.");
            return;
        }

        if (!this.patients.equals(newPatients)) {
            Logger.log(Logger.LogLevel.INFO, "Patientenliste aktualisiert. Anzahl: " + newPatients.size());
            this.patients = new ArrayList<>(newPatients);
            fireTableDataChanged();
        } else {
            Logger.log(Logger.LogLevel.INFO, "Keine Änderungen in der Patientenliste festgestellt.");
        }
    }

    /**
     * Gibt den Patienten an einem bestimmten Zeilenindex zurück.
     *
     * @param rowIndex Der Index der Zeile (0-basiert).
     * @return Der Patient an der angegebenen Zeile oder {@code null}, wenn der Index ungültig ist.
     */
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
