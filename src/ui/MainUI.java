// Optimierte und dokumentierte MainUI-Klasse
package ui;

import db.DAO;
import db.DBConnection;
import db.Search;
import model.Patient;
import utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Die MainUI-Klasse stellt die grafische Benutzeroberfläche für die Verwaltung von Patientendaten bereit.
 * Sie umfasst Funktionen zum Anzeigen, Hinzufügen, Bearbeiten, Löschen und Suchen von Patienten.
 */
public class MainUI extends JFrame {

    private final DAO dao;
    private JPanel mainPanel;
    private JTable table;

    /**
     * Initialisiert die Hauptbenutzeroberfläche zur Patientenverwaltung.
     *
     * @param dao Das DAO-Objekt für Datenbankoperationen.
     */
    public MainUI(DAO dao) {
        super("Patientenverwaltung");
        this.dao = dao;

        Logger.log(Logger.LogLevel.INFO, "MainUI wird initialisiert.");

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Logger.log(Logger.LogLevel.INFO, "Programm wird geschlossen.");
                DBConnection.closeConnection();
                System.exit(0);
            }
        });

        initializeComponents();

        setContentPane(mainPanel);
        setSize(900, 400);
        setLocationRelativeTo(null);

        loadAllPatientsAsync();
    }

    /**
     * Initialisiert alle UI-Komponenten.
     */
    private void initializeComponents() {
        Logger.log(Logger.LogLevel.INFO, "UI-Komponenten werden initialisiert.");

        mainPanel = new JPanel(new BorderLayout());
        table = new JTable(new TableModel());
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(createToolBar(), BorderLayout.NORTH);
    }

    /**
     * Erstellt die Werkzeugleiste mit Schaltflächen für Benutzeraktionen.
     *
     * @return Die initialisierte Werkzeugleiste.
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        toolBar.add(createButton("Hinzufügen", this::onAddPatient));
        toolBar.add(createButton("Bearbeiten", this::onEditPatient));
        toolBar.add(createButton("Löschen", this::onDeletePatient));
        toolBar.add(createButton("Aktualisieren", this::loadAllPatientsAsync));
        toolBar.add(createButton("Suchen", this::onSearchPatient));

        return toolBar;
    }

    /**
     * Erstellt eine Schaltfläche mit einem Action Listener.
     *
     * @param text     Der Text der Schaltfläche.
     * @param action   Die auszuführende Aktion beim Klick.
     * @return Eine konfigurierte JButton-Instanz.
     */
    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    /**
     * Öffnet den Dialog für die erweiterte Suche.
     */
    private void onSearchPatient() {
        // Erstelle ein Dialogfenster für die Suche
        JDialog searchDialog = new JDialog(this, "Patientensuche", true);
        searchDialog.setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel(new GridLayout(13, 2, 5, 5));

        // Felder für die Suche erstellen
        JTextField tfPatientID = new JTextField();
        JTextField tfVorname = new JTextField();
        JTextField tfNachname = new JTextField();
        JTextField tfGeburtsdatum = new JTextField();
        JTextField tfStrasse = new JTextField();
        JTextField tfPlz = new JTextField();
        JTextField tfOrt = new JTextField();
        JTextField tfBundeslandName = new JTextField();
        JTextField tfTelefon = new JTextField();
        JTextField tfGeschlechtName = new JTextField();
        JTextField tfKrankenkasseName = new JTextField();
        JTextField tfSonstiges = new JTextField();

        // Labels und Eingabefelder hinzufügen
        searchPanel.add(new JLabel("Patient ID:"));
        searchPanel.add(tfPatientID);
        searchPanel.add(new JLabel("Vorname:"));
        searchPanel.add(tfVorname);
        searchPanel.add(new JLabel("Nachname:"));
        searchPanel.add(tfNachname);
        searchPanel.add(new JLabel("Geburtsdatum:"));
        searchPanel.add(tfGeburtsdatum);
        searchPanel.add(new JLabel("Straße:"));
        searchPanel.add(tfStrasse);
        searchPanel.add(new JLabel("PLZ:"));
        searchPanel.add(tfPlz);
        searchPanel.add(new JLabel("Ort:"));
        searchPanel.add(tfOrt);
        searchPanel.add(new JLabel("Bundesland:"));
        searchPanel.add(tfBundeslandName);
        searchPanel.add(new JLabel("Telefon:"));
        searchPanel.add(tfTelefon);
        searchPanel.add(new JLabel("Geschlecht:"));
        searchPanel.add(tfGeschlechtName);
        searchPanel.add(new JLabel("Krankenkasse:"));
        searchPanel.add(tfKrankenkasseName);
        searchPanel.add(new JLabel("Sonstiges:"));
        searchPanel.add(tfSonstiges);

        searchDialog.add(searchPanel, BorderLayout.CENTER);

        // Buttons
        JButton btnSearch = new JButton("Suchen");
        JButton btnCancel = new JButton("Abbrechen");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnCancel);
        searchDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Suchlogik
        btnSearch.addActionListener(e -> {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("PatientID", tfPatientID.getText());
            criteria.put("Vorname", tfVorname.getText());
            criteria.put("Nachname", tfNachname.getText());
            criteria.put("Geburtsdatum", tfGeburtsdatum.getText());
            criteria.put("Strasse", tfStrasse.getText());
            criteria.put("PLZ", tfPlz.getText());
            criteria.put("Ort", tfOrt.getText());
            criteria.put("BundeslandName", tfBundeslandName.getText());
            criteria.put("Telefon", tfTelefon.getText());
            criteria.put("GeschlechtName", tfGeschlechtName.getText());
            criteria.put("KrankenkasseName", tfKrankenkasseName.getText());
            criteria.put("Sonstiges", tfSonstiges.getText());

            try {
                List<Patient> results = Search.searchPatients(criteria);
                ((TableModel) table.getModel()).setPatients(results);

                String criteriaSummary = criteria.entrySet().stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .map(entry -> entry.getKey() + ": *" + entry.getValue() + "*")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Keine Suchkriterien angegeben");

                JOptionPane.showMessageDialog(this, "Es wurde nach folgenden Kriterien gesucht:\n" + criteriaSummary);
                searchDialog.dispose();
            } catch (SQLException ex) {
                Logger.log(Logger.LogLevel.ERROR, "Fehler bei der Suche", ex);
                JOptionPane.showMessageDialog(this, "Fehler bei der Suche. Bitte versuchen Sie es erneut.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Schließen-Button
        btnCancel.addActionListener(e -> searchDialog.dispose());

        // Dialog-Einstellungen
        searchDialog.pack();
        searchDialog.setLocationRelativeTo(this);
        searchDialog.setVisible(true);
    }


    /**
     * Öffnet den Dialog zum Hinzufügen eines neuen Patienten.
     */
    private void onAddPatient() {
        Logger.log(Logger.LogLevel.INFO, "Dialog zum Hinzufügen eines Patienten wird geöffnet.");
        Dialog dialog = new Dialog(this, "Neuen Patienten hinzufügen", null);
        dialog.setVisible(true);

        Patient newPatient = dialog.getPatient();
        if (newPatient != null) {
            try {
                dao.addPatient(newPatient);
                Logger.log(Logger.LogLevel.INFO, "Patient hinzugefügt: " + newPatient);
                loadAllPatientsAsync();
            } catch (Exception e) {
                handleError("Fehler beim Hinzufügen des Patienten.", e);
            }
        }
    }

    /**
     * Öffnet den Dialog zum Bearbeiten eines ausgewählten Patienten.
     */
    private void onEditPatient() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showMessage("Bitte wählen Sie einen Patienten zur Bearbeitung aus.", "Warnung", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Patient selected = ((TableModel) table.getModel()).getPatientAt(row);
        Dialog dialog = new Dialog(this, "Patient bearbeiten", selected);
        dialog.setVisible(true);

        Patient updatedPatient = dialog.getPatient();
        if (updatedPatient != null) {
            try {
                dao.updatePatient(updatedPatient);
                Logger.log(Logger.LogLevel.INFO, "Patient aktualisiert: " + updatedPatient);
                loadAllPatientsAsync();
            } catch (Exception e) {
                handleError("Fehler beim Aktualisieren des Patienten.", e);
            }
        }
    }

    /**
     * Löscht den ausgewählten Patienten nach Bestätigung.
     */
    private void onDeletePatient() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showMessage("Bitte wählen Sie einen Patienten zum Löschen aus.", "Warnung", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Sind Sie sicher, dass Sie diesen Patienten löschen möchten?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Patient selected = ((TableModel) table.getModel()).getPatientAt(row);
            try {
                dao.deletePatient(selected.getPatientID());
                Logger.log(Logger.LogLevel.INFO, "Patient gelöscht: ID=" + selected.getPatientID());
                loadAllPatientsAsync();
            } catch (Exception e) {
                handleError("Fehler beim Löschen des Patienten.", e);
            }
        }
    }

    /**
     * Lädt alle Patienten asynchron aus der Datenbank und aktualisiert die Tabelle.
     */
    private void loadAllPatientsAsync() {
        Logger.log(Logger.LogLevel.INFO, "Lade alle Patienten.");
        SwingWorker<List<Patient>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Patient> doInBackground() {
                return dao.getAllPatients();
            }

            @Override
            protected void done() {
                try {
                    List<Patient> patients = get();
                    ((TableModel) table.getModel()).setPatients(patients);
                    Logger.log(Logger.LogLevel.INFO, "Patienten geladen: " + patients.size());
                } catch (Exception e) {
                    handleError("Fehler beim Laden der Patienten.", e);
                }
            }
        };
        worker.execute();
    }

    /**
     * Zeigt eine Fehlermeldung an und protokolliert den Fehler.
     *
     * @param message Die anzuzeigende Fehlermeldung.
     * @param e       Die zu protokollierende Ausnahme.
     */
    private void handleError(String message, Exception e) {
        Logger.log(Logger.LogLevel.ERROR, message, e);
        showMessage(message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Zeigt eine Nachricht in einem Dialog an.
     *
     * @param message Die anzuzeigende Nachricht.
     * @param title   Der Titel des Dialogs.
     * @param type    Der Nachrichtentyp (z. B. JOptionPane.INFORMATION_MESSAGE).
     */
    private void showMessage(String message, String title, int type) {
        if (type != JOptionPane.INFORMATION_MESSAGE &&
                type != JOptionPane.WARNING_MESSAGE &&
                type != JOptionPane.ERROR_MESSAGE &&
                type != JOptionPane.QUESTION_MESSAGE &&
                type != JOptionPane.PLAIN_MESSAGE) {
            throw new IllegalArgumentException("Ungültiger Nachrichtentyp angegeben.");
        }
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    /**
     * Hauptmethode der Anwendung.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Logger.log(Logger.LogLevel.INFO, "Anwendung wird gestartet.");
        SwingUtilities.invokeLater(() -> new MainUI(new DAO()).setVisible(true));
    }
}
