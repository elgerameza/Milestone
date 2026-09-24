package edu.gcu.cst239.meza.gerardo.milestone_app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class to centralize console input operations.
 * <p>
 * Reading user input from the console and validating it requires
 * repetitive try/catch and range checking logic.  This class groups
 * these operations into reusable methods so the rest of the
 * application can focus on core logic rather than on error
 * handling.  The methods provided here prompt the user, read a
 * value and enforce simple validation rules.
 */
public class InputUtilities {
    /**
     * Scanner used for all input operations.  The same scanner is
     * reused to avoid exhausting underlying system resources.  The
     * scanner is intentionally not closed because closing it would
     * close {@link System#in} as well.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a line of text from the console without any validation.
     *
     * @param prompt the message to display before reading
     * @return the text entered by the user
     */
    public String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    /**
     * Reads an integer from the console.  The method will prompt the user
     * repeatedly until a valid integer is entered.
     *
     * @param prompt the message to display before reading
     * @return the integer value entered by the user
     */
    public int readInt(String prompt) {
        int result = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println(prompt);
                result = Integer.parseInt(scanner.nextLine().trim());
                isValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return result;
    }

    /**
     * Reads an integer within a specific range from the console.  The method
     * will continue prompting the user until a valid integer within the
     * specified range is entered.
     *
     * @param prompt the message to display before reading
     * @param min    the minimum acceptable value (inclusive)
     * @param max    the maximum acceptable value (inclusive)
     * @return the integer value entered by the user within the given range
     */
    public int readInt(String prompt, int min, int max) {
        int result = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println(prompt);
                result = Integer.parseInt(scanner.nextLine().trim());
                if (result >= min && result <= max) {
                    isValid = true;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return result;
    }

    /**
     * Reads a double from the console.  The method will continue
     * prompting until a valid floating point number is entered.
     *
     * @param prompt the message to display
     * @return the double value entered by the user
     */
    public double readDouble(String prompt) {
        double result = 0.0;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println(prompt);
                result = Double.parseDouble(scanner.nextLine().trim());
                isValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
        return result;
    }

    /**
     * Reads a double within a specific range from the console.  The method
     * will continue prompting until a valid number within the given
     * range is entered.
     *
     * @param prompt the message to display
     * @param min    the minimum acceptable value (inclusive)
     * @param max    the maximum acceptable value (inclusive)
     * @return the double entered by the user within the given range
     */
    public double readDouble(String prompt, double min, double max) {
        double result = 0.0;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println(prompt);
                result = Double.parseDouble(scanner.nextLine().trim());
                if (result >= min && result <= max) {
                    isValid = true;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
        return result;
    }

    /**
     * Reads a boolean value from the console.  The user must enter
     * "true" or "false" (case insensitive).  The method will continue
     * prompting until a valid boolean is entered.
     *
     * @param prompt the message to display
     * @return the boolean value entered by the user
     */
    public boolean readBoolean(String prompt) {
        boolean result = false;
        boolean isValid = false;
        while (!isValid) {
            System.out.println(prompt + " (true/false):");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                result = Boolean.parseBoolean(input);
                isValid = true;
            } else {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
            }
        }
        return result;
    }

    /**
     * Reads a {@link LocalDate} from the console using the provided
     * date pattern.  The method will prompt the user until a date in
     * the correct format is entered.
     *
     * @param prompt  the message to display
     * @param pattern the date format pattern (e.g. "yyyy-MM-dd")
     * @return the date entered by the user
     */
    public LocalDate readLocalDate(String prompt, String pattern) {
        LocalDate result = null;
        boolean isValid = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        while (!isValid) {
            try {
                System.out.println(prompt + " (format: " + pattern + ")");
                result = LocalDate.parse(scanner.nextLine().trim(), formatter);
                isValid = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid input. Please enter a date in the format " + pattern + ".");
            }
        }
        return result;
    }

    /**
     * Reads a list of strings from the console.  This method prompts the
     * user repeatedly until they enter an empty line to signal completion.
     * Each non empty line is added to the list.  Useful when
     * collecting a list of ingredients or similar multi value fields.
     *
     * @param prompt the message to display before beginning input
     * @return a list of strings entered by the user (possibly empty)
     */
    public List<String> readStringList(String prompt) {
        List<String> list = new ArrayList<>();
        System.out.println(prompt + " (enter blank line to finish):");
        while (true) {
            String value = scanner.nextLine().trim();
            if (value.isEmpty()) {
                break;
            }
            list.add(value);
        }
        return list;
    }
}
