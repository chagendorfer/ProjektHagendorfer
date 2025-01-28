package model;

import utils.Logger;
import java.time.LocalDate;

/**
 * Repräsentiert einen Patienten in der Anwendung.
 * Diese Klasse enthält alle relevanten Informationen eines Patienten, die in der Datenbank gespeichert werden.
 */
public class Patient {

    /** Die eindeutige ID des Patienten. */
    private int patientID;

    /** Der Vorname des Patienten. */
    private String vorname;

    /** Der Nachname des Patienten. */
    private String nachname;

    /** Die Anrede des Patienten (z. B. Herr, Frau). */
    private String anrede;

    /** Das Geburtsdatum des Patienten im Format YYYY-MM-DD. */
    private LocalDate geburtsdatum;

    /** Die Straße und Hausnummer des Patienten. */
    private String strasse;

    /** Die Postleitzahl des Wohnorts des Patienten. */
    private String plz;

    /** Der Wohnort des Patienten. */
    private String ort;

    /** Die ID des Bundeslandes des Wohnorts des Patienten. */
    private int bundeslandID;

    /** Der Name des Bundeslandes des Wohnorts des Patienten. */
    private String bundeslandName;

    /** Die Telefonnummer des Patienten. */
    private String telefon;

    /** Die ID des Geschlechts des Patienten. */
    private int geschlechtID;

    /** Der Name des Geschlechts des Patienten. */
    private String geschlechtName;

    /** Die ID der Krankenkasse des Patienten. */
    private int krankenkasseID;

    /** Der Name der Krankenkasse des Patienten. */
    private String krankenkasseName;

    /** Weitere Informationen über den Patienten. */
    private String sonstiges;

    /**
     * Leerer Konstruktor für neue Patienten.
     */
    public Patient() {
        Logger.log(Logger.LogLevel.DEBUG, "Neues Patient-Objekt erstellt (ohne Parameter).");
    }

    /**
     * Vollständiger Konstruktor mit allen Feldern.
     */
    public Patient(int patientID, String vorname, String nachname, String anrede, LocalDate geburtsdatum, String strasse, String plz, String ort, int bundeslandID, String telefon, int geschlechtID, int krankenkasseID, String sonstiges) {
        this.patientID = patientID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.anrede = anrede;
        this.geburtsdatum = geburtsdatum;
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
        this.bundeslandID = bundeslandID;
        this.telefon = telefon;
        this.geschlechtID = geschlechtID;
        this.krankenkasseID = krankenkasseID;
        this.sonstiges = sonstiges;
        Logger.log(Logger.LogLevel.DEBUG, "Patient-Objekt erstellt: " + this);
    }

    // Getter und Setter mit vollständiger Dokumentation und Logging
    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        if (this.patientID == 0 || this.patientID != patientID) {
            Logger.log(Logger.LogLevel.DEBUG, "PatientID geändert: " + this.patientID + " -> " + patientID);
            this.patientID = patientID;
        }
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        if (this.vorname == null || !this.vorname.equals(vorname)) {
            Logger.log(Logger.LogLevel.DEBUG, "Vorname geändert: " + this.vorname + " -> " + vorname);
            this.vorname = vorname;
        }
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        if (this.nachname == null || !this.nachname.equals(nachname)) {
            Logger.log(Logger.LogLevel.DEBUG, "Nachname geändert: " + this.nachname + " -> " + nachname);
            this.nachname = nachname;
        }
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        if (this.anrede == null || !this.anrede.equals(anrede)) {
            Logger.log(Logger.LogLevel.DEBUG, "Anrede geändert: " + this.anrede + " -> " + anrede);
            this.anrede = anrede;
        }
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        if (this.geburtsdatum == null || !this.geburtsdatum.equals(geburtsdatum)) {
            Logger.log(Logger.LogLevel.DEBUG, "Geburtsdatum geändert: " + this.geburtsdatum + " -> " + geburtsdatum);
            this.geburtsdatum = geburtsdatum;
        }
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        if (this.strasse == null || !this.strasse.equals(strasse)) {
            Logger.log(Logger.LogLevel.DEBUG, "Straße geändert: " + this.strasse + " -> " + strasse);
            this.strasse = strasse;
        }
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        if (this.plz == null || !this.plz.equals(plz)) {
            Logger.log(Logger.LogLevel.DEBUG, "PLZ geändert: " + this.plz + " -> " + plz);
            this.plz = plz;
        }
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        if (this.ort == null || !this.ort.equals(ort)) {
            Logger.log(Logger.LogLevel.DEBUG, "Ort geändert: " + this.ort + " -> " + ort);
            this.ort = ort;
        }
    }

    public int getBundeslandID() {
        return bundeslandID;
    }

    public void setBundeslandID(int bundeslandID) {
        if (this.bundeslandID == 0 || this.bundeslandID != bundeslandID) {
            Logger.log(Logger.LogLevel.DEBUG, "BundeslandID geändert: " + this.bundeslandID + " -> " + bundeslandID);
            this.bundeslandID = bundeslandID;
        }
    }

    public String getBundeslandName() {
        return bundeslandName;
    }

    public void setBundeslandName(String bundeslandName) {
        if (this.bundeslandName == null || !this.bundeslandName.equals(bundeslandName)) {
            Logger.log(Logger.LogLevel.DEBUG, "BundeslandName geändert: " + this.bundeslandName + " -> " + bundeslandName);
            this.bundeslandName = bundeslandName;
        }
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        if (this.telefon == null || !this.telefon.equals(telefon)) {
            Logger.log(Logger.LogLevel.DEBUG, "Telefon geändert: " + this.telefon + " -> " + telefon);
            this.telefon = telefon;
        }
    }

    public int getGeschlechtID() {
        return geschlechtID;
    }

    public void setGeschlechtID(int geschlechtID) {
        if (this.geschlechtID == 0 || this.geschlechtID != geschlechtID) {
            Logger.log(Logger.LogLevel.DEBUG, "GeschlechtID geändert: " + this.geschlechtID + " -> " + geschlechtID);
            this.geschlechtID = geschlechtID;
        }
    }

    public String getGeschlechtName() {
        return geschlechtName;
    }

    public void setGeschlechtName(String geschlechtName) {
        if (this.geschlechtName == null || !this.geschlechtName.equals(geschlechtName)) {
            Logger.log(Logger.LogLevel.DEBUG, "GeschlechtName geändert: " + this.geschlechtName + " -> " + geschlechtName);
            this.geschlechtName = geschlechtName;
        }
    }

    public int getKrankenkasseID() {
        return krankenkasseID;
    }

    public void setKrankenkasseID(int krankenkasseID) {
        if (this.krankenkasseID == 0 || this.krankenkasseID != krankenkasseID) {
            Logger.log(Logger.LogLevel.DEBUG, "KrankenkasseID geändert: " + this.krankenkasseID + " -> " + krankenkasseID);
            this.krankenkasseID = krankenkasseID;
        }
    }

    public String getKrankenkasseName() {
        return krankenkasseName;
    }

    public void setKrankenkasseName(String krankenkasseName) {
        if (this.krankenkasseName == null || !this.krankenkasseName.equals(krankenkasseName)) {
            Logger.log(Logger.LogLevel.DEBUG, "KrankenkasseName geändert: " + this.krankenkasseName + " -> " + krankenkasseName);
            this.krankenkasseName = krankenkasseName;
        }
    }

    public String getSonstiges() {
        return sonstiges;
    }

    public void setSonstiges(String sonstiges) {
        if (this.sonstiges == null || !this.sonstiges.equals(sonstiges)) {
            Logger.log(Logger.LogLevel.DEBUG, "Sonstiges geändert: " + this.sonstiges + " -> " + sonstiges);
            this.sonstiges = sonstiges;
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID=" + patientID +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", anrede='" + anrede + '\'' +
                ", geburtsdatum=" + geburtsdatum +
                ", strasse='" + strasse + '\'' +
                ", plz='" + plz + '\'' +
                ", ort='" + ort + '\'' +
                ", bundeslandID=" + bundeslandID +
                ", bundeslandName='" + bundeslandName + '\'' +
                ", telefon='" + telefon + '\'' +
                ", geschlechtID=" + geschlechtID +
                ", geschlechtName='" + geschlechtName + '\'' +
                ", krankenkasseID=" + krankenkasseID +
                ", krankenkasseName='" + krankenkasseName + '\'' +
                ", sonstiges='" + sonstiges + '\'' +
                '}';
    }
}