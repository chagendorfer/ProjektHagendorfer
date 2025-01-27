package ui;

import model.Patient;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PatientTableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "ID", "Vorname", "Nachname", "Anrede", "Geburtsdatum",
            "Straße", "PLZ", "Ort", "BundeslandID",
            "Telefon", "GeschlechtID", "KrankenkasseID", "Sonstiges"
    };

    private List<Patient> patients = new ArrayList<>();

    @Override
    public int getRowCount() {
        return patients.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Patient p = patients.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> p.getPatientID();
            case 1 -> p.getVorname();
            case 2 -> p.getNachname();
            case 3 -> p.getAnrede();
            case 4 -> p.getGeburtsdatum();   // LocalDate
            case 5 -> p.getStrasse();
            case 6 -> p.getPlz();
            case 7 -> p.getOrt();
            case 8 -> p.getBundeslandID();
            case 9 -> p.getTelefon();
            case 10 -> p.getGeschlechtID();
            case 11 -> p.getKrankenkasseID();
            case 12 -> p.getSonstiges();
            default -> "";
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
        fireTableDataChanged();
    }

    public Patient getPatientAt(int rowIndex) {
        return patients.get(rowIndex);
    }
}
