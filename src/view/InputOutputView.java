package view;

import model.Player;
import model.Property;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputOutputView {
    private static Scanner scanner = new Scanner(System.in);
    public static String promptInput(String prompt, String[] options) {
        String input;
        while(true) {
            System.out.println(prompt);
            System.out.print("> ");
            input = scanner.next();
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
    public static int promptGetPlayerByID(ArrayList<Player> players, int totalPlayer) {
        String[] playerIdOptions = new String[totalPlayer];
        InputOutputView.displayMessage("Type the player ID (number on the left of the player's name)");
        for (int i = 0; i < totalPlayer; i++) {
            playerIdOptions[i] = String.valueOf(i + 1);
            InputOutputView.displayMessage(i + 1 + ". " + players.get(i).getName());
        }
        return Integer.parseInt(InputOutputView.promptInput("", playerIdOptions));
    }
    public static String promptFilename(String prompt) {
        String input;
        do{
            System.out.println(prompt);
            System.out.print("> ");
            input = scanner.next();
        } while(input.contains("/"));
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
            displayMessage("The " + i + "th property's name is: " + property.getName() + ", price is: " + property.getPrice() + ", rent is " + property.getRent());
        }
    }
    /**
     * @param property means the property to be printed out
     */
    public static void displayProperty(Property property) {
        displayMessage("The property's name is: " + property.getName() + ", price is: " + property.getPrice() + ", rent is " + property.getRent());
    }
    /**
     * @param prompt The prompt for user to specify the format of input
     * @return a legal value as the update (only contains 0-9 and English Alphabet)
     */
    public static String promptString(String prompt) {
        String input;
        Pattern pattern = Pattern.compile("\\p{Alnum}+");
        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            input = scanner.next();
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                return input;
            }
            System.out.println("Invalid name! Please double check the format and retype.");
        }
    }
    /**
     * @param prompt The prompt for user to specify the format of input
     * @return a legal value as the update (positive integer)
     */
    public static int promptInteger(String prompt) {
        String input;
        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            input = scanner.next();
            try {
                int newValue = Integer.parseInt(input);
                if(newValue <= 0) {
                    System.out.println("Invalid value! Please double check the format and retype a positive integer.");
                } else {
                    return newValue;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid value! Please double check the format and retype a positive integer.");
            }
        }
    }

}
