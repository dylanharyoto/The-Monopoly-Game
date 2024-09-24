package game;

import player.Player;
import square.Property;
import square.Square;

import java.util.*;


public class Gameboard {
    private Scanner scanner;
    private ArrayList<Player> players;
    private ArrayList<Square> squares;
    private int round;
    private int currentPlayerId;
    private int goPosition;

    public Gameboard() {
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<Player>(6);
        this.squares = new ArrayList<Square>(20);
        this.round = 1;
        this.currentPlayerId = -1;
        goPosition = -1;
    }

    public ArrayList<Player> getAllPlayers() {
        return this.players;
    }

    public Player getPlayerById(int playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }

    private Player getNextPlayer() {
        if (this.round == 0) {
            return players.get(1);
        }
        if (currentPlayerId == players.size())
            round++;
        currentPlayerId = ++currentPlayerId % players.size();
        return players.get(currentPlayerId);
    }
    
    private Square getSquareByPosition(int position) {
        if (position < 20 && position >= 0)
            return this.squares.get(position);
        else
            return null;
    }

    public int getPositionBySquareName (String name) {
        for (Square square : this.squares) {
            if (square.getName().equals(name)){
                return square.getPosition();
            }
            else {
                return -1; // -1 denotes that the given name of square is not defined or added to the current gameboard
            }
        }
    }



    public boolean checkGameStatus() {
        return this.round < 100 && (players.size() > 1 && players.size() < 7);
    }
    
    public void startGame() {
        System.out.println("Welcome to MonoPolyU, the game is starting...");
        while (checkGameStatus()) {
            Player currentPlayer = getNextPlayer();
            boolean proceed = false;
            int initialPosition = currentPlayer.getCurrentPosition();
            while (!proceed) {
                System.out.println("Now, it's " + currentPlayer.getName() + "'s turn.\nWould you like to\n1. Roll the dice\n2. Check player's status\n3. Print current board");
                System.out.print("> ");
                int choice = -1;
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Your input is not a valid number!");
                    scanner.next();
                    continue;
                }
                switch (choice) {
                    case 1:
                        currentPlayer.rollDice();
                        proceed = true;
                        break;
                    case 2:
                        System.out.println("Would you like to\n1. Check all players\n2. Check a single player");
                        System.out.print("> ");
                        break;
                    case 3:
                        this.display_board();
                        break;
                }
            }
            int currentPosition = currentPlayer.getCurrentPosition();
            if (initialPosition == currentPosition) {
                proceed = false;
                while (!proceed) {
                    try {
                        System.out.println("Would you like to \n1. Pay HKD$150 to get out\n2. Stay in jail");
                        System.out.print("> ");
                        int choice = scanner.nextInt();
                        if (choice == 1) {
                            currentPlayer.decreaseMoney(150);
                            currentPlayer.setInJailDuration(0);
                        }
                        proceed = true;
                    } catch (InputMismatchException | NumberFormatException e) {
                        System.out.println("Your input is not a valid number!");
                        scanner.next();
                    }
                }
            } else if(initialPosition < currentPosition) {
                if(goPosition <= currentPosition && goPosition > initialPosition) {
                    getSquareByPosition(goPosition).takeEffect(currentPlayer);
                }
            } else {
                if(goPosition <= currentPosition || goPosition > initialPosition) {
                    getSquareByPosition(goPosition).takeEffect(currentPlayer);
                }
            }
            getSquareByPosition(currentPosition).takeEffect(currentPlayer);
            if (currentPlayer.getMoney() < 0) {
                for (Property property : currentPlayer.getProperties()) {
                    property.setOwner(null);
                }
                System.out.println(currentPlayer.getName() + " Out!");
                players.remove(currentPlayer);
                currentPlayerId = -1;
            }
        }
        endGame();
    }

    public void endGame() {
        if (players.size() == 1)
            System.out.println("Game Finished! The winner is " + players.get(0).getName());
        else {
            System.out.print("Game Finished! The winners are ");
            for (int i = 0; i < players.size(); i++) {
                System.out.print(players.get(i).getName());
                if (i != players.size() - 1)
                    System.out.print(", ");
            }
            System.out.println(".");
        }
    }


    public void display_board() {
        String empty_board = "                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n---------------------+---------------------+---------------------+---------------------+---------------------+---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+                                                                                       +---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+                                        MONOPOLY                                       +---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+                                                                                       +---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+---------------------+---------------------+---------------------+---------------------+---------------------\n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     ";
        System.out.println(empty_board);
    }

    // private String map_to_json(Map<String, Object> map) {
    //     StringBuilder json = new StringBuilder("{");
    //     for (Map.Entry<String, Object> entry : map.entrySet()) {
    //         json.append("\"").append(entry.getKey()).append("\":");
    //         if (entry.getValue() instanceof Map) {
    //             json.append(map_to_json((Map<String, Object>) entry.getValue()));
    //         } else {
    //             json.append("\"").append(entry.getValue()).append("\"");
    //         }
    //         json.append(",");
    //     }
    //     json.deleteCharAt(json.length() - 1); // Remove the trailing comma
    //     json.append("}");
    //     return json.toString();
    // }

}
