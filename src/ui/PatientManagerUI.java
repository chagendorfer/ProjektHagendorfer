package ui;

import db.PatientDAO;
import model.Patient;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PatientManagerUI extends JFrame {
    private final PatientDAO patientDAO;  // Zugriff auf DB

    // UI-Komponenten
    private JPanel mainPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JToolBar toolBar;

    public PatientManagerUI(PatientDAO dao) {
        super("Patientenverwaltung (Swing)");
        this.patientDAO = dao;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                onExit(); // onExit() beim Schließen aufrufen
            }
        });

        initComponents();

        // Einstellungen für das Fenster
        setContentPane(mainPanel);
        setSize(900, 400);
        setLocationRelativeTo(null); // Bildschirmzentrierung
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Daten laden
        loadAllPatientsAsync();
    }

    private void initComponents() {
        // Hauptpanel
        mainPanel = new JPanel(new BorderLayout());

        // Tabelle und Scrollpane
        table = new JTable(new PatientTableModel());
        scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        /*
        // Menüleiste
        menuBar = createMenuBar();
        setJMenuBar(menuBar);
         */

        // Toolbar
        toolBar = createToolBar();
        mainPanel.add(toolBar, BorderLayout.NORTH);
    }

    /*
    private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Datei");
        JMenuItem menuExit = new JMenuItem("Beenden");
        menuExit.addActionListener(e -> onExit());
        menuFile.add(menuExit);
        menuBar.add(menuFile);

        JMenu menuActions = new JMenu("Aktionen");
        JMenuItem menuAdd = new JMenuItem("Neuer Patient");
        menuAdd.addActionListener(e -> onAddPatient());
        JMenuItem menuEdit = new JMenuItem("Bearbeiten");
        menuEdit.addActionListener(e -> onEditPatient());
        JMenuItem menuDelete = new JMenuItem("Löschen");
        menuDelete.addActionListener(e -> onDeletePatient());
        JMenuItem menuRefresh = new JMenuItem("Aktualisieren");
        menuRefresh.addActionListener(e -> loadAllPatientsAsync());

        menuActions.add(menuAdd);
        menuActions.add(menuEdit);
        menuActions.add(menuDelete);
        menuActions.addSeparator();
        menuActions.add(menuRefresh);

        menuBar.add(menuActions);
        return menuBar;
    }
     */

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> onAddPatient());
        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> onEditPatient());
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> onDeletePatient());
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadAllPatientsAsync());

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);
        return toolBar;
    }

    private void onExit() {
        try {
            patientDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dispose();

        System.exit(0);
    }

    private void onAddPatient() {
        PatientDialog dialog = new PatientDialog(this, "Neuer Patient", null);
        dialog.setVisible(true);

        Patient newPatient = dialog.getPatient();
        if (newPatient != null) {
            try {
                patientDAO.addPatient(newPatient);
                loadAllPatientsAsync();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Ungültige Eingabe", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Hinzufügen des Patienten!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditPatient() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bitte einen Patienten auswählen!");
            return;
        }

        Patient selected = ((PatientTableModel) table.getModel()).getPatientAt(row);
        PatientDialog dialog = new PatientDialog(this, "Patient bearbeiten", selected);
        dialog.setVisible(true);

        Patient updatedPatient = dialog.getPatient();
        if (updatedPatient != null) {
            try {
                patientDAO.updatePatient(updatedPatient);
                loadAllPatientsAsync();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Aktualisieren des Patienten!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDeletePatient() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bitte einen Patienten auswählen!");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(this, "Wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            Patient selected = ((PatientTableModel) table.getModel()).getPatientAt(row);
            try {
                patientDAO.deletePatient(selected.getPatientID());
                loadAllPatientsAsync();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Löschen des Patienten!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadAllPatientsAsync() {
        SwingWorker<List<Patient>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Patient> doInBackground() throws Exception {
                return patientDAO.getAllPatients();
            }

            @Override
            protected void done() {
                try {
                    List<Patient> patients = get();
                    ((PatientTableModel) table.getModel()).setPatients(patients);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PatientManagerUI.this, "Fehler beim Laden der Patienten!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) throws SQLException {
        PatientDAO dao = new PatientDAO();
        SwingUtilities.invokeLater(() -> new PatientManagerUI(dao).setVisible(true));
    }
}
