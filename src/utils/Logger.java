package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Die Logger-Klasse dient zum Protokollieren von Nachrichten auf verschiedenen Log-Ebenen.
 * Unterstützt werden die Ebenen DEBUG, INFO, WARN und ERROR.
 * Die Nachrichten werden in eine Log-Datei geschrieben und gleichzeitig auf der Konsole ausgegeben.
 */
public class Logger {

    /** Pfad zur Log-Datei, die beim Initialisieren der Klasse generiert wird. */
    private static String logFile;

    /** Datumsformat für Zeitstempel in Log-Nachrichten. */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Datumsformat für die Log-Datei. */
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    /** Aktuelles Log-Level der Anwendung. Nur Nachrichten auf diesem Level oder höher werden protokolliert. */
    private static final LogLevel settingLevel = LogLevel.DEBUG;

    // Statischer Initialisierungsblock, der die Log-Datei beim Laden der Klasse erstellt.
    static {
        initializeLogFile();
    }

    /**
     * Die verfügbaren Log-Level, um die Wichtigkeit von Nachrichten zu definieren.
     */
    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG
    }

    /**
     * Initialisiert die Log-Datei. Erstellt das Verzeichnis und die Datei, falls diese nicht existieren.
     */
    private static void initializeLogFile() {
        String dateTime = LocalDateTime.now().format(FILE_DATE_FORMAT);
        logFile = "Logs/log_" + dateTime + ".log";

        try {
            File logFileObj = new File(logFile);
            File parentDir = logFileObj.getParentFile();
            if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
                throw new IOException("Log-Verzeichnis konnte nicht erstellt werden: " + parentDir.getAbsolutePath());
            }
            if (!logFileObj.exists() && !logFileObj.createNewFile()) {
                throw new IOException("Log-Datei konnte nicht erstellt werden: " + logFile);
            }
        } catch (IOException e) {
            System.err.println("Fehler bei der Initialisierung der Log-Datei: " + e.getMessage());
            logFile = null;
        }
    }

    /**
     * Protokolliert eine Nachricht auf dem angegebenen Log-Level.
     *
     * @param level   Der Log-Level der Nachricht.
     * @param message Die zu protokollierende Nachricht.
     */
    public static void log(LogLevel level, String message) {
        if (logFile == null) {
            System.err.println("Log-Datei ist nicht verfügbar. Nachricht: " + message);
            return;
        }

        if (checkLoggingLevel(level)) {
            String caller = getCallerInfo();
            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String logMessage = String.format("[%s] [%s] [%s] %s", timestamp, level, caller, message);
            System.out.println(logMessage);
            writeToFile(logMessage);
        }
    }

    /**
     * Protokolliert eine Nachricht und einen zugehörigen Fehler (Exception) auf dem angegebenen Log-Level.
     *
     * @param level     Der Log-Level der Nachricht.
     * @param message   Die zu protokollierende Nachricht.
     * @param throwable Der Fehler, der protokolliert werden soll.
     */
    public static void log(LogLevel level, String message, Throwable throwable) {
        if (logFile == null) {
            System.err.println("Log-Datei ist nicht verfügbar. Nachricht: " + message);
            return;
        }

        if (checkLoggingLevel(level)) {
            String caller = getCallerInfo();
            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String logMessage = String.format("[%s] [%s] [%s] %s", timestamp, level, caller, message);
            System.out.println(logMessage);
            throwable.printStackTrace(System.out);
            writeToFile(logMessage);
            writeToFile(getStackTraceAsString(throwable));
        }
    }

    /**
     * Konvertiert einen Stacktrace in einen lesbaren String.
     *
     * @param throwable Der Fehler (Throwable), dessen Stacktrace konvertiert werden soll.
     * @return Der Stacktrace als String.
     */
    private static String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder("Stacktrace:\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
        }
        return sb.toString();
    }

    /**
     * Schreibt eine Nachricht in die Log-Datei.
     *
     * @param logMessage Die zu schreibende Nachricht.
     */
    private static void writeToFile(String logMessage) {
        try (FileWriter fileWriter = new FileWriter(logFile, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(logMessage);
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben ins Logfile: " + e.getMessage());
        }
    }

    /**
     * Ermittelt Informationen über die Methode, die den Logger aufgerufen hat.
     *
     * @return Ein String im Format "Klasse#Methode".
     */
    private static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            return String.format("%s#%s", stackTrace[3].getClassName(), stackTrace[3].getMethodName());
        }
        return "UnknownCaller";
    }

    /**
     * Überprüft, ob eine Nachricht auf dem angegebenen Log-Level protokolliert werden soll.
     *
     * @param level Der Log-Level der Nachricht.
     * @return true, wenn der Log-Level niedrig genug ist, um protokolliert zu werden, ansonsten false.
     */
    private static boolean checkLoggingLevel(LogLevel level) {
        return enumToNumberValue(level) <= enumToNumberValue(settingLevel);
    }

    /**
     * Konvertiert einen Log-Level in eine numerische Priorität.
     *
     * @param level Der Log-Level.
     * @return Die numerische Priorität des Levels.
     */
    private static int enumToNumberValue(LogLevel level) {
        return switch (level) {
            case ERROR -> 0;
            case WARN -> 1;
            case INFO -> 2;
            case DEBUG -> 3;
        };
    }
}
