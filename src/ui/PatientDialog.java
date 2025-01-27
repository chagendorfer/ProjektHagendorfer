package ui;

import model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class PatientDialog extends JDialog {

    private Patient patient;

    // GUI-Felder
    private final JTextField tfVorname = new JTextField(15);
    private final JTextField tfNachname = new JTextField(15);
    private final JTextField tfAnrede = new JTextField(10);
    private final JTextField tfGeburtsdatum = new JTextField(10); // im Format YYYY-MM-DD
    private final JTextField tfStrasse = new JTextField(20);
    private final JTextField tfPlz = new JTextField(6);
    private final JTextField tfOrt = new JTextField(15);
    private final JTextField tfBundeslandID = new JTextField(5);
    private final JTextField tfTelefon = new JTextField(15);
    private final JTextField tfGeschlechtID = new JTextField(5);
    private final JTextField tfKrankenkasseID = new JTextField(5);
    private final JTextField tfSonstiges = new JTextField(20);

    public PatientDialog(Frame owner, String title, Patient p) {
        super(owner, title, true);  // modal
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
        mainPanel.add(new JLabel("Bundesland-ID:"));
        mainPanel.add(tfBundeslandID);
        mainPanel.add(new JLabel("Telefon:"));
        mainPanel.add(tfTelefon);
        mainPanel.add(new JLabel("Geschlecht-ID:"));
        mainPanel.add(tfGeschlechtID);
        mainPanel.add(new JLabel("Krankenkasse-ID:"));
        mainPanel.add(tfKrankenkasseID);
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
            tfVorname.setText(p.getVorname());
            tfNachname.setText(p.getNachname());
            tfAnrede.setText(p.getAnrede());
            tfGeburtsdatum.setText(p.getGeburtsdatum() != null ? p.getGeburtsdatum().toString() : "");
            tfStrasse.setText(p.getStrasse());
            tfPlz.setText(p.getPlz());
            tfOrt.setText(p.getOrt());
            tfBundeslandID.setText(String.valueOf(p.getBundeslandID()));
            tfTelefon.setText(p.getTelefon());
            tfGeschlechtID.setText(String.valueOf(p.getGeschlechtID()));
            tfKrankenkasseID.setText(String.valueOf(p.getKrankenkasseID()));
            tfSonstiges.setText(p.getSonstiges());
        }

        // Event-Handling
        btnOk.addActionListener(e -> {
            if (validateInputs()) {
                savePatientData();  // speichert in this.patient
                dispose();
            }
        });

        btnCancel.addActionListener(e -> {
            patient = null; // Abbruch = kein Resultat
            dispose();
        });

        pack();
        setLocationRelativeTo(owner);

        // Wenn das Fenster mit [x] geschlossen wird → Abbruch
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                patient = null;
            }
        });
    }

    // Beim Bearbeiten wollen wir nicht versehentlich am Original herumbasteln
    private Patient clonePatient(Patient p) {
        Patient copy = new Patient();
        copy.setPatientID(p.getPatientID());
        copy.setVorname(p.getVorname());
        copy.setNachname(p.getNachname());
        copy.setAnrede(p.getAnrede());
        copy.setGeburtsdatum(p.getGeburtsdatum());
        copy.setStrasse(p.getStrasse());
        copy.setPlz(p.getPlz());
        copy.setOrt(p.getOrt());
        copy.setBundeslandID(p.getBundeslandID());
        copy.setTelefon(p.getTelefon());
        copy.setGeschlechtID(p.getGeschlechtID());
        copy.setKrankenkasseID(p.getKrankenkasseID());
        copy.setSonstiges(p.getSonstiges());
        return copy;
    }

    // Eingaben checken
    private boolean validateInputs() {
        if (tfVorname.getText().trim().isEmpty() || tfNachname.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vorname und Nachname dürfen nicht leer sein!",
                    "Ungültige Eingabe", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(tfBundeslandID.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "BundeslandID muss eine Zahl sein!",
                    "Ungültige Eingabe", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }


    // Patient-Objekt mit den Dialogwerten füllen
    private void savePatientData() {
        if (patient == null) {
            patient = new Patient();
        }
        patient.setVorname(tfVorname.getText().trim());
        patient.setNachname(tfNachname.getText().trim());
        patient.setAnrede(tfAnrede.getText().trim());

        String geburtsdatumText = tfGeburtsdatum.getText().trim();
        if (!geburtsdatumText.isEmpty()) {
            LocalDate date = LocalDate.parse(geburtsdatumText); // Exception falls Format falsch
            patient.setGeburtsdatum(date);
        } else {
            patient.setGeburtsdatum(null);
        }

        patient.setStrasse(tfStrasse.getText().trim());
        patient.setPlz(tfPlz.getText().trim());
        patient.setOrt(tfOrt.getText().trim());

        try {
            patient.setBundeslandID(Integer.parseInt(tfBundeslandID.getText().trim()));
        } catch (NumberFormatException e) {
            patient.setBundeslandID(0);
        }

        patient.setTelefon(tfTelefon.getText().trim());

        try {
            patient.setGeschlechtID(Integer.parseInt(tfGeschlechtID.getText().trim()));
        } catch (NumberFormatException e) {
            patient.setGeschlechtID(1); // default?
        }

        try {
            patient.setKrankenkasseID(Integer.parseInt(tfKrankenkasseID.getText().trim()));
        } catch (NumberFormatException e) {
            patient.setKrankenkasseID(0);
        }

        patient.setSonstiges(tfSonstiges.getText().trim());
    }

    /** Gibt das neue/aktualisierte Patient-Objekt zurück oder null bei Abbruch */
    public Patient getPatient() {
        return patient;
    }
}
