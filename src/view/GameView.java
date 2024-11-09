package view;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Player;
import model.Property;

public class GameView {
    private static final Scanner scanner = new Scanner(System.in);
    public static String inputPrompt(String prompt, String[] options) {
        String input;
        while(true) {
            System.out.println(prompt);
            System.out.print("> ");
            input = scanner.next();
            for(String option : options) {
                if(option.equals(input)) {
                    return input;
                }
            }
            System.out.println("Invalid answer! Please double check the available options and retype.");
        }
    }



    /**
     * @param properties means an ArrayList of properties to be printed out in order
     */
    public static void displayAllProperties(ArrayList <Property> properties) {
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);
            System.out.println(i + "th property's name is: " + property.getName() + ", price is: " + property.getPrice() + ", rent is " + property.getRent());
        }
    }

    /**
     * @param property means the property to be printed out
     */
    public static void displayProperty(Property property) {

        System.out.println(" The property's name is: " + property.getName() + ", price is: " + property.getPrice() + ", rent is " + property.getRent());

    }


    /**
     * @param prompt The prompt for user to specify the format of input
     * @return a legal value as the update (only contains 0-9 and English Alphabet)
     */
    public static String updateName (String prompt) {
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
    public static int updateInteger (String prompt) {
        String input;

        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            input = scanner.next();

            try {
                int newValue = Integer.parseInt(input);
                if(newValue <= 0) {
                    System.out.println("Invalid value! Please double check the format and retype a positive integer.");
                }
                else {
                    return newValue;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid value! Please double check the format and retype a positive integer.");
            }

        }
    }

    public String promptBuyProperty(Player player, String propertyName, int price) {
        return inputPrompt("Hi " + player.getName() + ", would you like to buy " + propertyName + " for HKD " + price + "?\n1. Yes\n2. No", new String[]{"1", "2"});
    }
    public void displayPlayers(ArrayList<Player> players) {
        if (players == null || players.isEmpty()) {
            System.out.println("No players to display.");
            return;
        }
        for (Player player : players) {
            System.out.println(player.getName() + " - Money: HKD " + player.getMoney());
        }
    }
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
