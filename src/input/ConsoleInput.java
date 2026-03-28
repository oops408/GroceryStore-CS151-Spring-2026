package input;

import java.util.Scanner;

public final class ConsoleInput {

    private ConsoleInput() {
    }

    public static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("EXIT")) {
            gracefulExit(scanner);
        }

        return input;
    }

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            String input = readLine(scanner, prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            String input = readLine(scanner, prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static void gracefulExit(Scanner scanner) {
        System.out.println("EXIT keyword detected. Closing the Grocery Store System. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}
