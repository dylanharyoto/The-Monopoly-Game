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

    public void joinPlayer(Player player){
        this.players.add(player);
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
                        while (!proceed) {
                            System.out.println("Would you like to\n1. Check all players\n2. Check a single player");
                            System.out.print("> ");
                            try {
                                choice = scanner.nextInt();
                            } catch (InputMismatchException | NumberFormatException e) {
                                System.out.println("Your input is not a valid number!");
                                scanner.next();
                                continue;
                            }
                            switch (choice) {
                                case 1:
                                    for(Player player : players) {
                                        player.getPlayer();
                                    }
                                    proceed = true;
                                    break;
                                case 2:
                                    while (!proceed) {
                                        System.out.print("Would you like to check");
                                        for(int i=0;i<players.size();i++) {
                                            System.out.print(" "+(i+1)+"."+players.get(i).getName()+" ");
                                        }
                                        System.out.println("\n> ");
                                        try {
                                            choice = scanner.nextInt();
                                        } catch (InputMismatchException | NumberFormatException e) {
                                            System.out.println("Your input is not a valid number!");
                                            scanner.next();
                                            continue;
                                        }
                                        if(choice>=1 && choice<=players.size()) {
                                            players.get(choice-1).getPlayer();
                                            proceed = true;
                                        }
                                        else {
                                            System.out.println("Your choice is out of range!");
                                        }
                                    }
                                    proceed = false;
                                    break;
                            }
                        }
                        proceed = false;
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
        //21*5
        String emptyBoard = "                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n---------------------+---------------------+---------------------+---------------------+---------------------+---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+                                                                                       +---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+                                        MONOPOLY                                       +---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+                                                                                       +---------------------\n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n                     |                                                                                       |                     \n---------------------+---------------------+---------------------+---------------------+---------------------+---------------------\n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     \n                     |                     |                     |                     |                     |                     ";
        int[] blockIndex = {0,22,44,66,88,110,902,1694,2486,3278,4070,4048,4026,4004,3982,3960,3168,2376,1594,792};


        System.out.println(emptyBoard);
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
