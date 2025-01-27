package model;

import java.time.LocalDate;
import java.util.Map;

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

    /** Die Telefonnummer des Patienten. */
    private String telefon;

    /** Die ID des Geschlechts des Patienten. */
    private int geschlechtID;

    /** Die ID der Krankenkasse des Patienten. */
    private int krankenkasseID;

    /** Weitere Informationen über den Patienten. */
    private String sonstiges;

    /**
     * Leerer Konstruktor für neue Patienten.
     */
    public Patient() {

    }

    /**
     * Vollständiger Konstruktor mit allen Feldern.
     *
     * @param patientID         Die eindeutige ID des Patienten.
     * @param vorname           Der Vorname des Patienten.
     * @param nachname          Der Nachname des Patienten.
     * @param anrede            Die Anrede des Patienten (z. B. Herr, Frau).
     * @param geburtsdatum      Das Geburtsdatum des Patienten.
     * @param strasse           Die Straße mit Hausnummer, in der der Patient wohnt.
     * @param plz               Die Postleitzahl des Wohnorts.
     * @param ort               Der Ort, in dem der Patient wohnt.
     * @param bundeslandID      Die ID des Bundeslandes, in dem der Patient lebt.
     * @param telefon           Die Telefonnummer des Patienten.
     * @param geschlechtID      Die ID des Geschlechts des Patienten.
     * @param krankenkasseID    Die ID der Krankenversicherung des Patienten.
     * @param sonstiges         Sonstige Informationen über den Patienten.
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
        }


    private static final Map<Integer, String> GESCHLECHT_MAP = Map.of(
            1, "Männlich",
            2, "Weiblich",
            3, "Divers"
    );

    /**
     * Methode zum Abrufen der Geschlechts-Bezeichnung.
     *
     * @param id Geschlechts-ID
     * @return Bezeichnung als String (z. B. "Männlich", "Weiblich", etc.)
     */
    public static String getGeschlechtBezeichnung(int id) {
        return GESCHLECHT_MAP.getOrDefault(id, "Unbekannt");
    }

    /**
     * Methode, um die ID anhand der Geschlechts-Bezeichnung zu finden.
     *
     * @param bezeichnung z. B. "Männlich", "Weiblich", "Divers"
     * @return Geschlechts-ID oder -1, wenn nicht gefunden
     */
    public static int getId(String bezeichnung) {
        return GESCHLECHT_MAP.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(bezeichnung))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    // Getter und Setter

    /**
     * Gibt die ID des Patienten zurück.
     * @return Die ID des Patienten.
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * Setzt die ID des Patienten.
     * @param patientID Die neue ID des Patienten.
     */
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    /**
     * Gibt den Vornamen des Patienten zurück.
     * @return Der Vorname des Patienten.
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Setzt den Vornamen des Patienten.
     * @param vorname Der neue Vorname des Patienten.
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Gibt den Nachnamen des Patienten zurück.
     * @return Der Nachname des Patienten.
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * Setzt den Nachnamen des Patienten.
     * @param nachname Der neue Nachname des Patienten.
     */
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Gibt die Anrede des Patienten zurück.
     * @return Die Anrede des Patienten.
     */
    public String getAnrede() {
        return anrede;
    }

    /**
     * Setzt die Anrede des Patienten.
     * @param anrede Die neue Anrede des Patienten.
     */
    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    /**
     * Gibt das Geburtsdatum des Patienten zurück.
     *
     * @return Das Geburtsdatum des Patienten.
     */
    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    /**
     * Setzt das Geburtsdatum des Patienten.
     * @param geburtsdatum Das neue Geburtsdatum des Patienten.
     */
    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    /**
     * Gibt die Straße des Patienten zurück.
     * @return Die Straße des Patienten.
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Setzt die Straße des Patienten.
     * @param strasse Die neue Straße des Patienten.
     */
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    /**
     * Gibt die Postleitzahl des Patienten zurück.
     * @return Die Postleitzahl des Patienten.
     */
    public String getPlz() {
        return plz;
    }

    /**
     * Setzt die Postleitzahl des Patienten.
     * @param plz Die neue Postleitzahl des Patienten.
     */
    public void setPlz(String plz) {
        this.plz = plz;
    }

    /**
     * Gibt den Wohnort des Patienten zurück.
     * @return Der Wohnort des Patienten.
     */
    public String getOrt() {
        return ort;
    }

    /**
     * Setzt den Wohnort des Patienten.
     * @param ort Der neue Wohnort des Patienten.
     */
    public void setOrt(String ort) {
        this.ort = ort;
    }

    /**
     * Gibt die Bundesland-ID des Patienten zurück.
     * @return Die Bundesland-ID des Patienten.
     */
    public int getBundeslandID() {
        return bundeslandID;
    }

    /**
     * Setzt die Bundesland-ID des Patienten.
     * @param bundeslandID Die neue Bundesland-ID des Patienten.
     */
    public void setBundeslandID(int bundeslandID) {
        this.bundeslandID = bundeslandID;
    }

    /**
     * Gibt die Telefonnummer des Patienten zurück.
     * @return Die Telefonnummer des Patienten.
     */
    public String getTelefon() {
        return telefon;
    }

    /**
     * Setzt die Telefonnummer des Patienten.
     * @param telefon Die neue Telefonnummer des Patienten.
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    /**
     * Gibt die Geschlechts-ID des Patienten zurück.
     * @return Die Geschlechts-ID des Patienten.
     */
    public int getGeschlechtID() {
        return geschlechtID;
    }

    /**
     * Setzt die Geschlechts-ID des Patienten.
     * @param geschlechtID Die neue Geschlechts-ID des Patienten.
     */
    public void setGeschlechtID(int geschlechtID) {
        this.geschlechtID = geschlechtID;
    }

    /**
     * Gibt die Krankenkassen-ID des Patienten zurück.
     * @return Die Krankenkassen-ID des Patienten.
     */
    public int getKrankenkasseID() {
        return krankenkasseID;
    }

    /**
     * Setzt die Krankenkassen-ID des Patienten.
     * @param krankenkasseID Die neue Krankenkassen-ID des Patienten.
     */
    public void setKrankenkasseID(int krankenkasseID) {
        this.krankenkasseID = krankenkasseID;
    }

    /**
     * Gibt sonstige Informationen des Patienten zurück.
     * @return Sonstige Informationen des Patienten.
     */
    public String getSonstiges() {
        return sonstiges;
    }

    /**
     * Setzt sonstige Informationen des Patienten.
     * @param sonstiges Neue sonstige Informationen des Patienten.
     */
    public void setSonstiges(String sonstiges) {
        this.sonstiges = sonstiges;
    }

    /**
     * Gibt eine String-Darstellung des Patienten-Objekts zurück.
     * @return String-Darstellung des Patienten.
     */
    @Override
    public String toString() {
        return "Patient{" +
               "patientID=" + patientID +
               ", vorname='" + vorname + '\'' +
               ", nachname='" + nachname + '\'' +
               ", anrede='" + anrede + '\'' +
               ", geburtsdatum='" + geburtsdatum + '\'' +
               ", strasse='" + strasse + '\'' +
               ", plz='" + plz + '\'' +
               ", ort='" + ort + '\'' +
               ", bundeslandID=" + bundeslandID +
               ", telefon='" + telefon + '\'' +
               ", geschlechtID=" + geschlechtID +
               ", krankenkasseID=" + krankenkasseID +
               ", sonstiges='" + sonstiges + '\'' +
               '}';
    }
}
