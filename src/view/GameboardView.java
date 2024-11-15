package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.*;

public class GameboardView {
    private String gameboardString = """
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
    public void displayAllPlayers(ArrayList<Player> players) {
        if (players == null || players.isEmpty()) {
            InputOutputView.displayMessage("No players to display.");
            return;
        }
        for (Player player : players) {
            displayAPlayer(player);
        }
    }
    public void displayAPlayer(Player player) {
        if (player == null) {
            InputOutputView.displayMessage("No player to display.");
            return;
        }
        InputOutputView.displayMessage(player.getName());
        InputOutputView.displayMessage("|---Money: HKD " + player.getMoney());
        InputOutputView.displayMessage("|---Position: " + player.getPosition());
        InputOutputView.displayMessage("|---Properties: ");
        ArrayList <Property> properties = player.getProperties();
        for (Property property : properties) {
            InputOutputView.displayMessage("    |---" + property.getName());
        }
    }
    public void displayGameboard() {
        InputOutputView.displayMessage(this.gameboardString);
    }
    public void updateGameboard(Gameboard gameboard){
        List<String>[] players = new ArrayList[20];
        for(int i = 0; i < 20; i++){
            players[i] = new ArrayList<>();
        }
        for(Player player : gameboard.getAllPlayers()) {
            players[player.getPosition() - 1].add("p" + player.getId());
        }
        for(int i = 0; i < 20; i++){
            int[] blockIndex = {4598, 4576, 4554, 4532, 4510, 4488, 3696, 2904, 2112, 1320, 528, 550, 572, 594, 616, 638, 1430, 2222, 3014, 3806};
            int position = blockIndex[i];
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem(String.join(",",players[i])) + gameboardString.substring(position + 21);
        }
    }
    public void replaceBlockBySquare(Square square) {
        int[] blockIndex = {4070, 4048, 4026, 4004, 3982, 3960, 3168, 2376, 1584, 792, 0, 22, 44, 66, 88, 110, 902, 1694, 2486, 3278};
        int position = blockIndex[square.getPosition() - 1];
        int rowLength = 132;
        if (square instanceof Property) {
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem(((Property) square).getName()) + gameboardString.substring(position + 21);
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("HKD " + ((Property) square).getPrice()) + gameboardString.substring(position + 21);
        } else if (square instanceof Go) {
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("GO!") + gameboardString.substring(position + 21);
        } else if (square instanceof Chance) {
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("?CHANCE?") + gameboardString.substring(position + 21);
        } else if (square instanceof GoJail) {
            position += 2 * rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("GO TO JAIL") + gameboardString.substring(position + 21);
        } else if (square instanceof IncomeTax) {
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("INCOME TAX") + gameboardString.substring(position + 21);
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("PAY 10%") + gameboardString.substring(position + 21);
        } else if (square instanceof FreeParking) {
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("FREE PARKING") + gameboardString.substring(position + 21);
        } else if (square instanceof InJailOrJustVisiting) {
            position += rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("IN JAIL") + gameboardString.substring(position + 21);
            position += 2 * rowLength;
            gameboardString = gameboardString.substring(0, position) + replaceLineByItem("JUST VISITING") + gameboardString.substring(position + 21);
        }
    }
    private String replaceLineByItem(String item) {
        int itemLength = item.length();
        String emptyLine = "                     ";
        return (emptyLine.substring(0, 10 - itemLength / 2) + item + emptyLine.substring(10 + (itemLength + 1) / 2));
    }
}
