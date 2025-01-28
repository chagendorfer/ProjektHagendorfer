package ui;

import db.DAO;
import model.Patient;
import utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class MainUI extends JFrame {
    private final DAO DAO;  // Zugriff auf DBConnection

    // UI-Komponenten
    private JPanel mainPanel;
    private JTable table;

    public MainUI(DAO dao) {
        super("Patientenverwaltung (Swing)");
        this.DAO = dao;

        Logger.log(Logger.LogLevel.INFO, "MainUI initialisiert.");

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    dao.close();
                } catch (SQLException ex) {
                    Logger.log(Logger.LogLevel.ERROR, "Fehler beim Schließen der Datenbankverbindung.", ex);
                }
                System.exit(0);
            }
        });

        initComponents();

        // Einstellungen für das Fenster
        setContentPane(mainPanel);
        setSize(900, 400);
        setLocationRelativeTo(null); // Bildschirmzentrierung

        // Daten laden
        loadAllPatientsAsync();
    }

    private void initComponents() {
        Logger.log(Logger.LogLevel.INFO, "UI-Komponenten werden initialisiert.");
        mainPanel = new JPanel(new BorderLayout());

        table = new JTable(new TableModel());
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JToolBar toolBar = createToolBar();
        mainPanel.add(toolBar, BorderLayout.NORTH);
    }

    private JToolBar createToolBar() {
        Logger.log(Logger.LogLevel.INFO, "ToolBar wird erstellt.");
        JToolBar toolBar = new JToolBar();
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> onAddPatient());
        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> onEditPatient());
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> onDeletePatient());
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadAllPatientsAsync());
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchPatientByID());

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);
        toolBar.add(btnSearch);
        return toolBar;
    }

    private void onAddPatient() {
        Logger.log(Logger.LogLevel.INFO, "Dialog zum Hinzufügen eines Patienten geöffnet.");
        Dialog dialog = new Dialog(this, "Neuer Patient", null);
        dialog.setVisible(true);

        Patient newPatient = dialog.getPatient();
        if (newPatient != null) {
            try {
                DAO.addPatient(newPatient);
                Logger.log(Logger.LogLevel.INFO, "Neuer Patient hinzugefügt: " + newPatient);
                loadAllPatientsAsync();
            } catch (IllegalArgumentException e) {
                Logger.log(Logger.LogLevel.WARN, "Ungültige Eingabe: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, e.getMessage(), "Ungültige Eingabe", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.ERROR, "Fehler beim Hinzufügen des Patienten.", e);
                JOptionPane.showMessageDialog(this, "Fehler beim Hinzufügen des Patienten!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditPatient() {
        Logger.log(Logger.LogLevel.INFO, "Patient wird bearbeitet.");
        int row = table.getSelectedRow();
        if (row < 0) {
            Logger.log(Logger.LogLevel.WARN, "Kein Patient zur Bearbeitung ausgewählt.");
            JOptionPane.showMessageDialog(this, "Bitte einen Patienten auswählen!");
            return;
        }

        Patient selected = ((TableModel) table.getModel()).getPatientAt(row);
        Dialog dialog = new Dialog(this, "Patient bearbeiten", selected);
        dialog.setVisible(true);

        Patient updatedPatient = dialog.getPatient();
        if (updatedPatient != null) {
            try {
                DAO.updatePatient(updatedPatient);
                Logger.log(Logger.LogLevel.INFO, "Patient aktualisiert: " + updatedPatient);
                loadAllPatientsAsync();
            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.ERROR, "Fehler beim Aktualisieren des Patienten.", e);
                JOptionPane.showMessageDialog(this, "Fehler beim Aktualisieren des Patienten!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDeletePatient() {
        Logger.log(Logger.LogLevel.INFO, "Patient wird gelöscht.");
        int row = table.getSelectedRow();
        if (row < 0) {
            Logger.log(Logger.LogLevel.WARN, "Kein Patient zur Löschung ausgewählt.");
            JOptionPane.showMessageDialog(this, "Bitte einen Patienten auswählen!");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(this, "Wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            Patient selected = ((TableModel) table.getModel()).getPatientAt(row);
            try {
                DAO.deletePatient(selected.getPatientID());
                Logger.log(Logger.LogLevel.INFO, "Patient gelöscht: ID=" + selected.getPatientID());
                loadAllPatientsAsync();
            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.ERROR, "Fehler beim Löschen des Patienten.", e);
                JOptionPane.showMessageDialog(this, "Fehler beim Löschen des Patienten!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchPatientByID() {
        try {
            String input = JOptionPane.showInputDialog(this, "Bitte geben Sie die Patienten-ID ein:", "Patient suchen", JOptionPane.QUESTION_MESSAGE);

            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keine gültige Patienten-ID eingegeben.", "Warnung", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Patienten-ID ein.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient patient = DAO.getPatientByID(id);
            if (patient != null) {
                JOptionPane.showMessageDialog(this, "Patient gefunden:\n" + patient, "Patient gefunden", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Kein Patient mit ID " + id + " gefunden.", "Nicht gefunden", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.ERROR, "Fehler beim Suchen eines Patienten.", e);
            JOptionPane.showMessageDialog(this, "Fehler beim Suchen des Patienten. Bitte versuchen Sie es später erneut.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void loadAllPatientsAsync() {
        Logger.log(Logger.LogLevel.INFO, "Lade Patienten aus der Datenbank...");
        SwingWorker<List<Patient>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Patient> doInBackground() {
                return DAO.getAllPatients();
            }

            @Override
            protected void done() {
                try {
                    List<Patient> patients = get();
                    ((TableModel) table.getModel()).setPatients(patients);
                    Logger.log(Logger.LogLevel.INFO, "Patienten erfolgreich geladen.");
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.ERROR, "Fehler beim Laden der Patienten.", e);
                    JOptionPane.showMessageDialog(
                            MainUI.this,
                            "Fehler beim Laden der Patienten!",
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        Logger.log(Logger.LogLevel.INFO, "Programm wird gestartet.");
        DAO dao = db.DAO.getInstance();
        SwingUtilities.invokeLater(() -> new MainUI(dao).setVisible(true));
    }
}
