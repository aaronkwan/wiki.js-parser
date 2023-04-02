package org.example;

public class CommandLine {
    public static void main(String[] args) {
        // Arguments: username, password, output_file. Optional flag: -d or --debug.
        // Check if invalid number of arguments:
        if (args.length < 3 || args.length > 4) {
            System.out.println("Error: Expected 3 arguments, but got " + args.length + ".");
            System.out.println("Usage: <username> <password> <output_file_path>");
            System.exit(1);
        }
        // Grab credentials:
        String username = args[0];
        String password = args[1];
        String filePath = args[2];
        // Optional flag:
        String optionalFlag = "";
        boolean printDebug = false;
        if (args.length == 4) {
            optionalFlag = args[3];
        }
        if (optionalFlag.equals("-d") || optionalFlag.equals("--debug")) {
            printDebug = true;
        }
        // Debug:
        if (printDebug) {
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("File path: " + filePath);
            System.out.println("Print Debug: " + printDebug);
        }
        // Results:
        int result = Parser.parseWikiToFile(username, password, filePath, printDebug);
        switch (result) {
            case 1:
                System.out.println("Error: Problem authenticating (check credentials)");
                break;
            case 2:
                System.out.println("Error: Unable to access file path: " + filePath);
                break;
            case 3:
                System.out.println("Error: Unable to acesss wiki.");
                break;
            default:
                System.out.println("Success @ " + filePath);
        }
    }
}
