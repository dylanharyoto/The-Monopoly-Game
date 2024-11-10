package view;

import model.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class InputView {

    private static Scanner scanner = new Scanner(System.in);
    public static String inputPrompt(String prompt, String[] options) {
        String input;
        while(true) {
            System.out.println(prompt);
            System.out.print("> ");
            input = scanner.next();
            if(options.length == 0) return input;
            for(String option : options) {
                if(option.equals(input)) {
                    return input;
                }
            }
            System.out.println("Invalid answer! Please double check the available options and retype.");
        }
    }

    public static int promptGetPlayer(ArrayList<Player> players, int totalPlayer) {
        String[] playerIdOptions = new String[totalPlayer];
        InputView.displayMessage("Type the player ID (number on the left hand side of the player's name)");
        for (int i = 0; i < totalPlayer; i++) {
            playerIdOptions[i] = String.valueOf(i + 1);
            InputView.displayMessage(i + 1 + ". " + players.get(i).getName());
        }
        return Integer.parseInt(InputView.inputPrompt("", playerIdOptions));
    }

    public static void displayMessage(String message) {
        System.out.println(message);
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

}
