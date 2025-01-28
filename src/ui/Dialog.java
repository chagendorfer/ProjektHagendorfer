package ui;

import model.Patient;
import utils.Logger;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class Dialog extends JDialog {

    private Patient patient;

    // GUI-Felder
    private final JTextField tfVorname = new JTextField(15);
    private final JTextField tfNachname = new JTextField(15);
    private final JTextField tfAnrede = new JTextField(10);
    private final JTextField tfGeburtsdatum = new JTextField(10); // im Format YYYY-MM-DD
    private final JTextField tfStrasse = new JTextField(20);
    private final JTextField tfPlz = new JTextField(6);
    private final JTextField tfOrt = new JTextField(15);
    private final JTextField tfBundeslandName = new JTextField(5);
    private final JTextField tfTelefon = new JTextField(15);
    private final JTextField tfGeschlechtName = new JTextField(5);
    private final JTextField tfKrankenkasseName = new JTextField(5);
    private final JTextField tfSonstiges = new JTextField(20);

    public Dialog(Frame owner, String title, Patient p) {
        super(owner, title, true);  // modal
        Logger.log(Logger.LogLevel.INFO, "Dialog geöffnet: " + title);
        this.patient = (p != null) ? clonePatient(p) : null; // Kopie, um Original nicht zu verändern

        // Panels
        JPanel mainPanel = new JPanel(new GridLayout(12, 2, 5, 5));
        mainPanel.add(new JLabel("Vorname:"));
        mainPanel.add(tfVorname);
        mainPanel.add(new JLabel("Nachname:"));
        mainPanel.add(tfNachname);
        mainPanel.add(new JLabel("Anrede:"));
        mainPanel.add(tfAnrede);
        mainPanel.add(new JLabel("Geburtsdatum (YYYY-MM-DD):"));
        mainPanel.add(tfGeburtsdatum);
        mainPanel.add(new JLabel("Straße:"));
        mainPanel.add(tfStrasse);
        mainPanel.add(new JLabel("PLZ:"));
        mainPanel.add(tfPlz);
        mainPanel.add(new JLabel("Ort:"));
        mainPanel.add(tfOrt);
        mainPanel.add(new JLabel("Bundesland:"));
        mainPanel.add(tfBundeslandName);
        mainPanel.add(new JLabel("Telefon:"));
        mainPanel.add(tfTelefon);
        mainPanel.add(new JLabel("Geschlecht:"));
        mainPanel.add(tfGeschlechtName);
        mainPanel.add(new JLabel("Krankenkasse:"));
        mainPanel.add(tfKrankenkasseName);
        mainPanel.add(new JLabel("Sonstiges:"));
        mainPanel.add(tfSonstiges);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Abbrechen");
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);

        // Layout zusammenbauen
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Alte Daten setzen (falls Bearbeiten)
        if (p != null) {
            Logger.log(Logger.LogLevel.INFO, "Bearbeiten-Modus für Patient ID: " + p.getPatientID());
            tfVorname.setText(p.getVorname());
            tfNachname.setText(p.getNachname());
            tfAnrede.setText(p.getAnrede());
            tfGeburtsdatum.setText(p.getGeburtsdatum() != null ? p.getGeburtsdatum().toString() : "");
            tfStrasse.setText(p.getStrasse());
            tfPlz.setText(p.getPlz());
            tfOrt.setText(p.getOrt());
            tfBundeslandName.setText(String.valueOf(p.getBundeslandName()));
            tfTelefon.setText(p.getTelefon());
            tfGeschlechtName.setText(String.valueOf(p.getGeschlechtName()));
            tfKrankenkasseName.setText(String.valueOf(p.getKrankenkasseName()));
            tfSonstiges.setText(p.getSonstiges());
        }

        // Event-Handling
        btnOk.addActionListener(e -> {
            Logger.log(Logger.LogLevel.INFO, "OK-Button geklickt.");
            if (validateInputs()) {
                savePatientData();  // speichert in this.patient
                Logger.log(Logger.LogLevel.INFO, "Patientendaten gespeichert: " + patient);
                dispose();
            }
        });

        btnCancel.addActionListener(e -> {
            Logger.log(Logger.LogLevel.INFO, "Abbrechen-Button geklickt.");
            patient = null; // Abbruch = kein Resultat
            dispose();
        });

        pack();
        setLocationRelativeTo(owner);

        // Wenn das Fenster mit [x] geschlossen wird → Abbruch
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Logger.log(Logger.LogLevel.INFO, "Dialog geschlossen (Abbruch).");
                patient = null;
            }
        });
    }

    // Beim Bearbeiten wollen wir nicht versehentlich am Original herumbasteln
    private Patient clonePatient(Patient p) {
        Logger.log(Logger.LogLevel.INFO, "Patient wird geklont: ID=" + p.getPatientID());
        Patient copy = new Patient();
        copy.setPatientID(p.getPatientID());
        copy.setVorname(p.getVorname());
        copy.setNachname(p.getNachname());
        copy.setAnrede(p.getAnrede());
        copy.setGeburtsdatum(p.getGeburtsdatum());
        copy.setStrasse(p.getStrasse());
        copy.setPlz(p.getPlz());
        copy.setOrt(p.getOrt());
        copy.setBundeslandName(p.getBundeslandName());
        copy.setTelefon(p.getTelefon());
        copy.setGeschlechtName(p.getGeschlechtName());
        copy.setKrankenkasseName(p.getKrankenkasseName());
        copy.setSonstiges(p.getSonstiges());
        return copy;
    }

    // Eingaben checken
    private boolean validateInputs() {
        Logger.log(Logger.LogLevel.INFO, "Eingaben werden validiert.");
        if (tfVorname.getText().trim().isEmpty() || tfNachname.getText().trim().isEmpty()) {
            Logger.log(Logger.LogLevel.WARN, "Vorname oder Nachname fehlt.");
            JOptionPane.showMessageDialog(this, "Vorname und Nachname dürfen nicht leer sein!",
                    "Ungültige Eingabe", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }


    // Patient-Objekt mit den Dialogwerten füllen
    private void savePatientData() {
        Logger.log(Logger.LogLevel.INFO, "Patientendaten werden übernommen.");
        if (patient == null) {
            patient = new Patient();
        }
        patient.setVorname(tfVorname.getText().trim());
        patient.setNachname(tfNachname.getText().trim());
        patient.setAnrede(tfAnrede.getText().trim());

        String geburtsdatumText = tfGeburtsdatum.getText().trim();
            if (!geburtsdatumText.isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(geburtsdatumText); // Exception falls Format falsch
                    patient.setGeburtsdatum(date);
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.ERROR, "Ungültiges Geburtsdatum-Format.", e);
                }
            } else {
            patient.setGeburtsdatum(null);
        }

        patient.setStrasse(tfStrasse.getText().trim());
        patient.setPlz(tfPlz.getText().trim());
        patient.setOrt(tfOrt.getText().trim());
        patient.setBundeslandName(tfBundeslandName.getText().trim());
        patient.setTelefon(tfTelefon.getText().trim());
        patient.setGeschlechtName(tfGeschlechtName.getText().trim());
        patient.setKrankenkasseName(tfKrankenkasseName.getText().trim());
        patient.setSonstiges(tfSonstiges.getText().trim());
    }

    public Patient getPatient() {
        return patient;
    }
}
