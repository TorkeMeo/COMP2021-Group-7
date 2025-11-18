package hk.edu.polyu.comp.comp2021.clevis;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private String htmlLogFile;
    private String txtLogFile;
    private int commandIndex;

    public Logger(String htmlLogFile, String txtLogFile) {
        this.htmlLogFile = htmlLogFile;
        this.txtLogFile = txtLogFile;
        this.commandIndex = 1;
        initializeLogFiles();
    }

    public void initializeLogFiles() {
        createDirectoryExists(htmlLogFile);
        createDirectoryExists(txtLogFile);

        try (PrintWriter htmlWriter = new PrintWriter(new FileWriter(htmlLogFile))) {
            htmlWriter.println("<!DOCTYPE html>");
            htmlWriter.println("<html>");
            htmlWriter.println("<head>");
            htmlWriter.println("    <title>Clevis Command Log</title>");
            htmlWriter.println("    <style>");
            htmlWriter.println("        table { border-collapse: collapse; width: 100%; }");
            htmlWriter.println("        th, td { border: 1px solid black; padding: 8px; text-align: left; }");
            htmlWriter.println("        th { background-color: #f2f2f2; }");
            htmlWriter.println("        tr:nth-child(even) { background-color: #f9f9f9; }");
            htmlWriter.println("    </style>");
            htmlWriter.println("</head>");
            htmlWriter.println("<body>");
            htmlWriter.println("    <h1>Clevis Command Execution Log</h1>");
            htmlWriter.println("    <p>Session started: " + getCurrentTimestamp() + "</p>");
            htmlWriter.println("    <table>");
            htmlWriter.println("        <tr>");
            htmlWriter.println("            <th>Index</th>");
            htmlWriter.println("            <th>Command</th>");
            htmlWriter.println("        </tr>");
            htmlWriter.flush();
        } catch (IOException e) {
            System.err.println("Error initializing HTML log: " + e.getMessage());
        }

        try (PrintWriter txtWriter = new PrintWriter(new FileWriter(txtLogFile))) {
            txtWriter.println("Clevis Command Execution Log");
            txtWriter.println("Session started: " + getCurrentTimestamp());
            txtWriter.println("==========================================");
            txtWriter.flush();
        } catch (IOException e) {
            System.err.println("Error initializing TXT log: " + e.getMessage());
        }
    }

    private void createDirectoryExists (String filePath){
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    public void logCommand(String command) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(htmlLogFile, true))) {
            writer.println("        <tr>");
            writer.println("            <td>" + commandIndex + "</td>");
            writer.println("            <td>" + escapeHtml(command) + "</td>");  // 转义HTML特殊字符
            writer.println("        </tr>");
        } catch (IOException e) {
            System.err.println("Error appending to HTML log: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(txtLogFile, true))) {
            writer.println(commandIndex + ". " + command);
        } catch (IOException e) {
            System.err.println("Error appending to TXT log: " + e.getMessage());
        }

        commandIndex++;
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void close() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(htmlLogFile, true))) {
            writer.println("    </table>");
            writer.println("    <p>Session ended: " + getCurrentTimestamp() + "</p>");
            writer.println("    <p>Total commands executed: " + (commandIndex - 1) + "</p>");
            writer.println("</body>");
            writer.println("</html>");
        } catch (IOException e) {
            System.err.println("Error finalizing HTML log: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(txtLogFile, true))) {
            writer.println("==========================================");
            writer.println("Session ended: " + getCurrentTimestamp());
            writer.println("Total commands executed: " + (commandIndex - 1));
        } catch (IOException e) {
            System.err.println("Error finalizing TXT log: " + e.getMessage());
        }
    }
}
