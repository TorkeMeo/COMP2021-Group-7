package hk.edu.polyu.comp.comp2021.clevis;

import hk.edu.polyu.comp.comp2021.clevis.model.Clevis;
import java.util.Scanner;

public class Application {

    public static void main(String[] args){
        String htmlLogFile = null;
        String txtLogFile = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-html":
                    if (i + 1 < args.length)
                        htmlLogFile = args[++i];
                    break;
                case "-txt":
                    if (i + 1 < args.length)
                        txtLogFile = args[++i];
                    break;
            }
        }

        if (htmlLogFile == null || txtLogFile == null) {
            System.out.println("Error: Both -html and -txt parameters are required");
            System.exit(1);
        }

        Clevis clevis = new Clevis();
        // Initialize and utilize the system
        clevis.initializeLogging(htmlLogFile, txtLogFile);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Clevis - Command Line Vector Graphics Software");
        System.out.println("Type 'quit' to exit");

        try {
            while (clevis.isRunning()) {
                System.out.print("clevis> ");
                if (!scanner.hasNextLine()) break;

                String command = scanner.nextLine().trim();
                if (!command.isEmpty())
                    clevis.processCommand(command);
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize Clevis: " + e.getMessage());
            System.exit(1);
        }

        scanner.close();
        clevis.closeLogging();
    }
}