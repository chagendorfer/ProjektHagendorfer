package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "patient_manager.log"; // Name der Log-Datei
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    // Log-Ebenen
    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG
    }

    // Methode zum Loggen
    public static void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);

        // In Konsole ausgeben
        System.out.println(logMessage);

        // In Datei schreiben
        writeToFile(logMessage);
    }

    // Überladung: Log mit Exception
    public static void log(LogLevel level, String message, Throwable throwable) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);

        // In Konsole ausgeben
        System.out.println(logMessage);
        throwable.printStackTrace(System.out);

        // In Datei schreiben
        writeToFile(logMessage);
        writeToFile(getStackTraceAsString(throwable));
    }

    // Stacktrace in String umwandeln
    private static String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("Stacktrace:\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
        }
        return sb.toString();
    }

    // Nachricht in Datei schreiben
    private static void writeToFile(String logMessage) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(logMessage);
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben ins Logfile: " + e.getMessage());
        }
    }
}