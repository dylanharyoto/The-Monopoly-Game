package view;

import java.util.ArrayList;
import java.util.Scanner;
import model.Player;
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
