package view;

import java.util.ArrayList;
import java.util.List;
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
    public void displayPlayers(ArrayList<Player> players) {
        if (players == null || players.isEmpty()) {
            InputView.displayMessage("No players to display.");
            return;
        }
        for (Player player : players) {
            displayPlayer(player);
        }
    }
    public void displayPlayer(Player player) {
        if (player == null) {
            InputView.displayMessage("No player to display.");
            return;
        }
        InputView.displayMessage(player.getName());
        InputView.displayMessage("    Money: HKD " + player.getMoney());
        InputView.displayMessage("    Position: " + player.getPosition());
    }
    public void displayGameboard() {
        InputView.displayMessage(this.boardString);
        
    }
    public void replaceBlockBySquare(Square square) {
        int[] blockIndex = {4070, 4048, 4026, 4004, 3982, 3960, 3168, 2376, 1584, 792, 0, 22, 44, 66, 88, 110, 902, 1694, 2486, 3278};

        int position = blockIndex[square.getPosition()-1];
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

    public void updateGameboard(Gameboard gameboard){
        List<String>[] players = new ArrayList[20];
        for(int i=0; i<20; i++){
            players[i] = new ArrayList<>();
        }
        for(Player p: gameboard.getAllPlayers())
            players[p.getPosition()-1].add("p"+p.getId());
        for(int i=0; i<20; i++){
            int[] blockIndex = {4598, 4576, 4554, 4532, 4510, 4488, 3696, 2904, 2112, 1320, 528, 550, 572, 594, 616, 638, 1430, 2222, 3014, 3806};
            int position = blockIndex[i];
            boardString = boardString.substring(0, position) + replaceLineByItem(String.join(",",players[i]))
                    + boardString.substring(position + 21);
        }
    }

    //private void

    private String replaceLineByItem(String item) {
        int itemLength = item.length();
        String emptyLine = "                     ";
        return (emptyLine.substring(0, 10 - itemLength / 2) + item + emptyLine.substring(10 + (itemLength + 1) / 2));
    }
}
