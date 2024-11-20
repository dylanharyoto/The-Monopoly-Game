package main.view;

import main.model.Property;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputOutputView {
    private static Scanner scanner = new Scanner(System.in);
    private static String SIMULATED_INPUT;

    public static void setInput(String input) {
        SIMULATED_INPUT = input;
    }

    public static String promptInput(String prompt, String[] options) {
        String input;
        while(true) {
            System.out.println(prompt);
            System.out.print("> ");
            if (SIMULATED_INPUT != null) {
                input = SIMULATED_INPUT.split("\n")[0];
                if(SIMULATED_INPUT.contains("\n")) {
                    SIMULATED_INPUT = SIMULATED_INPUT.substring(SIMULATED_INPUT.indexOf("\n")+1, SIMULATED_INPUT.length());
                }
            }
            else {
                input = scanner.next();
                scanner.nextLine();
            }
            if(options.length == 0) {
                return input;
            }
            for(String option : options) {
                if(option.equals(input)) {
                    return input;
                }
            }
            System.out.println("Invalid answer! Please double check the available options and retype.");
        }
    }
    public static String promptFilename(String prompt) {

        String input = "/";
        while(input.contains("/")) {
            System.out.println(prompt);
            System.out.print("> ");
            if (SIMULATED_INPUT != null) {
                input = SIMULATED_INPUT.split("\n")[0];
                if(SIMULATED_INPUT.contains("\n")) {
                    SIMULATED_INPUT = SIMULATED_INPUT.substring(SIMULATED_INPUT.indexOf("\n")+1, SIMULATED_INPUT.length());
                }
            }
            else {
                input = scanner.next();
                scanner.nextLine();
            }
        }
        return input;
    }
    public static void displayMessage(String message) {
        System.out.println(message);
    }
    /**
     * @param properties means an ArrayList of properties to be printed out in order
     */
    public static void displayAllProperties(ArrayList <Property> properties) {
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);
            displayMessage(i + ". " + property.getName() + "\n|---Price: " + property.getPrice() + " | Rent: " + property.getRent());
        }
    }
    /**
     * @param property means the property to be printed out
     */
    public static void displayProperty(Property property) {
        displayMessage("The property's name is " + property.getName() + ", price is " + property.getPrice() + ", and rent is " + property.getRent());
    }
    /**
     * @param prompt The prompt for user to specify the format of input
     * @return a legal value as the update (only contains 0-9 and English Alphabet)
     */
    public static String promptString(String prompt) {
        String input;
        Pattern pattern = Pattern.compile("\\p{Alnum}+");
        while(true) {
            System.out.println(prompt);
            System.out.print("> ");
            if (SIMULATED_INPUT != null) {
                input = SIMULATED_INPUT.split("\n")[0];
                if(SIMULATED_INPUT.contains("\n")) {
                    SIMULATED_INPUT = SIMULATED_INPUT.substring(SIMULATED_INPUT.indexOf("\n")+1, SIMULATED_INPUT.length());
                }
            }
            else {
                input = scanner.next();
                scanner.nextLine();
            }
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                return input;
            }
            System.out.println("Invalid answer! Please double check the format and retype a legal name.");
        }

    }
    /**
     * @param prompt The prompt for user to specify the format of input
     * @return a legal value as the update (positive integer)
     */
    public static int promptInteger(String prompt) {
        String input;
        while(true) {
            System.out.println(prompt);
            System.out.print("> ");
            if (SIMULATED_INPUT != null) {
                input = SIMULATED_INPUT.split("\n")[0];
                if(SIMULATED_INPUT.contains("\n")) {
                    SIMULATED_INPUT = SIMULATED_INPUT.substring(SIMULATED_INPUT.indexOf("\n")+1, SIMULATED_INPUT.length());
                }

            } else {
                input = scanner.next();
                scanner.nextLine();
            }
            try {
                int newValue = Integer.parseInt(input);
                if (newValue <= 0) {
                    System.out.println("Invalid value! Please double check the format and retype a positive integer.");
                } else {
                    return newValue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid value! Please double check the format and retype a positive integer.");
            }
        }
    }

}
