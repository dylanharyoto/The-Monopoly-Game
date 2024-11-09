package view;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import model.*;

public class GameboardView {
    private static final Scanner scanner = new Scanner(System.in);
    private String boardString = """
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s
                ---------------------+---------------------+---------------------+---------------------+---------------------+---------------------
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                ---------------------+                                                                                       +---------------------
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                ---------------------+                                        MONOPOLY                                       +---------------------
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                ---------------------+                                                                                       +---------------------
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                                     |                                                                                       |                    \s
                ---------------------+---------------------+---------------------+---------------------+---------------------+---------------------
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s
                                     |                     |                     |                     |                     |                    \s""";

    public String inputPrompt(String prompt, String[] options) {
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

    public int promptGetPlayer(int totalPlayer) {
        String[] playerIdOptions = new String[totalPlayer];
        for (int i = 0; i < totalPlayer; i++) {
            playerIdOptions[i] = String.valueOf(i + 1);
        }
        return Integer.parseInt(inputPrompt("Please enter the player ID", playerIdOptions));
    }
    public String promptFilename(String prompt) {
        return inputPrompt(prompt, new String[]{""});
    }
    public String getFilenameInput() {
        return "";
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
    public void displayPlayer(Player player) {
        if (player == null) {
            System.out.println("No player to display.");
            return;
        }
    }
    public void displayMessage(String message) {
        System.out.println(message);
    }
    public void displayGameboard() {
        System.out.println(this.boardString);

    }
    public void replaceBlockBySquare(Square square) {
        int[] blockIndex = { 0, 22, 44, 66, 88, 110, 902, 1694, 2486, 3278, 4070, 4048, 4026, 4004, 3982, 3960, 3168, 2376, 1594, 792 };

        int position = blockIndex[square.getPosition()];
        int rowLength = 132;
        if (square instanceof Property) {
            position += rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem(((Property) square).getName())
                    + boardString.substring(position + 21);
            position += rowLength;
            boardString = boardString.substring(0, position)
                    + replaceLineByItem("HKD " + ((Property) square).getPrice()) + boardString.substring(position + 21);
        } else if (square instanceof Go) {
            position += rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("GO!")
                    + boardString.substring(position + 21);
        } else if (square instanceof Chance) {
            position += rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("?CHANCE?")
                    + boardString.substring(position + 21);
        } else if (square instanceof GoJail) {
            position += 2 * rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("GO TO JAIL")
                    + boardString.substring(position + 21);
        } else if (square instanceof IncomeTax) {
            position += rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("INCOME TAX")
                    + boardString.substring(position + 21);
            position += rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("PAY 10%")
                    + boardString.substring(position + 21);
        } else if (square instanceof FreeParking) {
            position += rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("FREE PARKING")
                    + boardString.substring(position + 21);
        } else if (square instanceof InJailOrJustVisiting) {
            position += rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("IN JAIL")
                    + boardString.substring(position + 21);
            position += 2 * rowLength;
            boardString = boardString.substring(0, position) + replaceLineByItem("JUST VISITING")
                    + boardString.substring(position + 21);
        }
    }
    private String replaceLineByItem(String item) {
        int itemLength = item.length();
        String emptyLine = "                     ";
        return (emptyLine.substring(0, 10 - itemLength / 2) + item + emptyLine.substring(10 + (itemLength + 1) / 2));
    }
}
