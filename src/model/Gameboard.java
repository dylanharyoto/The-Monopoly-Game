package model;

import java.io.*;
import java.util.*;

import controller.Main;

public class Gameboard {
    private Scanner scanner;
    private ArrayList<Player> players;
    private ArrayList<Square> squares;
    private int round;
    private int currentPlayerId;
    private int goPosition;
    private String boardString;
    private int[] blockIndex = { 0, 22, 44, 66, 88, 110, 902, 1694, 2486, 3278, 4070, 4048, 4026, 4004, 3982, 3960,
            3168, 2376, 1594, 792 };
    private static int count = 0;

    public Gameboard() {
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<Player>(6);
        this.squares = new ArrayList<Square>(20);
        this.round = 1;
        this.currentPlayerId = -1;

        this.goPosition = -1;
        Gameboard.count += 1;
        boardString = """
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
        boolean proceed = false;
        while(!proceed) {
            int choice = Integer.parseInt(Main.inputPrompt("Would you like to start a new game or load from an existing game?\n1. new game\n2.load game","1,2",scanner));
            if(choice == 1) {
                if(newGame()) proceed = true;
            }else if(choice == 2) {
                while(!proceed){
                    String filename = Main.inputPrompt("Please enter your file name here",",",scanner);
                    filename = filename.endsWith(".json") ? filename : filename + ".json";
                    if(loadGame(filename, 1)) proceed = true;
                    startGame();
                }
            }
        }
    }

    public void joinPlayer(Player player) {
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

    /*
     * private int getPositionBySquareName (String name) {
     * for (Square square : this.squares) {
     * if (square.getName().equals(name)){
     * return square.getPosition();
     * }
     * else return -1; // -1 denotes that the given name of square is not defined or
     * added to the current gameboard
     * }
     * }
     */

    public boolean checkGameStatus() {
        return this.round < 100 && (players.size() > 1 && players.size() < 7);
    }

    public void startGame() {
        System.out.println("Welcome to MonoPolyU, the game is starting...");
        while (checkGameStatus()) {
            Player currentPlayer = getNextPlayer();
            boolean proceed = false;
            int initialPosition = currentPlayer.getCurrentPosition();
            String choice = Main.inputPrompt("Now, it's " + currentPlayer.getName()+ "'s turn.\nWould you like to\n1. Roll the dice\n2. Check player's status\n3. Print current board\n4. Save game", "1,2,3,4",scanner);
            switch (choice) {
                case "1":
                    currentPlayer.rollDice();
                    proceed = true;
                    break;
                case "2":
                    choice = Main.inputPrompt("Would you like to\n1. Check all players\n2. Check a single player", "1,2",scanner);

                    if(choice.equals("1")) {
                        for (Player player : players) {
                            player.getPlayer();
                        }
                        proceed = true;
                    }
                    else{
                        choice = "";
                        String player_display = "";
                        for(int i=0;i<players.size();i++) {
                            choice += i+1 + ",";
                            player_display += ("\n" + (i + 1) + "." + players.get(i).getName());
                        }
                        choice = choice.substring(0, choice.length()-1);
                        choice = Main.inputPrompt("Would you like to check"+player_display, choice,scanner);
                        players.get(Integer.parseInt(choice) - 1).getPlayer();
                    }
                    break;
                case "3":
                    this.displayBoard();
                    break;
                case "4":
                    String filename = Main.inputPrompt("Please input filename here",",",scanner);
                    saveGame(filename);
                    break;
            }

            int currentPosition = currentPlayer.getCurrentPosition();
            if (initialPosition == currentPosition) {
                choice = Main.inputPrompt("Would you like to \n1. Pay HKD$150 to get out\n2. Stay in jail", "1,2",scanner);
                if (choice.equals("1")) {
                    currentPlayer.decreaseMoney(150);
                    currentPlayer.setInJailDuration(0);
                }
            } else if (initialPosition < currentPosition) {
                if (goPosition <= currentPosition && goPosition > initialPosition) {
                    getSquareByPosition(goPosition).takeEffect(currentPlayer);
                }
            } else {
                if (goPosition <= currentPosition || goPosition > initialPosition) {
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

    private boolean newGame() {
        boolean proceed = false;
        ArrayList<String> names = new ArrayList<String>(0);
        String choice = Main.inputPrompt("The first step of starting a new game is to choose 2~6 players, please enter number of players below","2,3,4,5,6",scanner);

        int playerNumber = Integer.parseInt(choice);
        if (playerNumber >= 2 && playerNumber <= 6) {
            names = new ArrayList<String>(playerNumber);
            System.out.println("""
                    Now you can type in the names of the players.
                    You can type multiple names at a time seperated by ';' symbol (max name length is 10 characters)
                    Or type '!d#' where # is the id of the name you want to delete. (for example '!d2')
                    If you like to resize the number of player, type '!r'.""");

            while (!proceed) {
                System.out.println("When players are ready, type '!p' to proceed. Current players:");
                if (names.isEmpty())
                    System.out.println("None");
                for (int i = 0; i < names.size(); i++) {
                    System.out.println((i + 1) + ". " + names.get(i));
                }
                System.out.print("> ");
                String nameInput = scanner.next();
                if (nameInput.startsWith("!d")) {
                    int deleteChoice = -1;
                    try {
                        deleteChoice = Integer.parseInt(nameInput.substring(2, 3));
                        if (deleteChoice < 1 || deleteChoice > names.size())
                            throw new NumberFormatException();
                    } catch (InputMismatchException | NumberFormatException e) {
                        System.out.println("Your input is invalid!");
                        continue;
                    }
                    names.remove(deleteChoice - 1);
                } else if (nameInput.equals("!r")) {
                    break;
                } else if (nameInput.equals("!p")) {
                    if (names.size() != playerNumber) {
                        System.out.println("Oops...There's still empty slot");
                        continue;
                    }
                    break;
                }else {
                    for (String substringName : nameInput.split(";")) {
                        if (substringName.length() > 10 || substringName.isEmpty())
                            System.out.println("One of the name length isn't permitted.");
                        else if (names.size() == playerNumber)
                            System.out.println("Slots are taken, player " + substringName.trim() + " is ignored.");
                        else
                            names.add(substringName.trim());
                    }
                }

            }
            while(!proceed){
                choice = Main.inputPrompt("Would you like to\n1. start with default map\n2. start by loading map", "1,2",scanner);

                if(choice.equals("1")) {
                    String curdir = System.getProperty("user.dir");
                    if(!loadGame(curdir+"/src/map1", 0))continue;
                    proceed = true;
                }
                else{
                    String filename = Main.inputPrompt("Please input the json filename", ",",scanner);
                    if(loadGame(filename, 0)){
                        System.out.println("File not exist!");
                        continue;
                    }
                    proceed = true;
                }

            }
            playerNumber = names.size();
        } else {
            System.out.println("This is not a valid number of players!");
        }

        for (int i = 0; i < names.size(); i++) {
            joinPlayer(new Player(i, names.get(i), 1500, 1));
        }
        startGame();
        return true;
    }

    public void saveGame(String filename) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        json.append("\"id\": ").append(Gameboard.count).append(",\n");

        json.append("\"players\": [\n");
        for (int i = 0; i < players.size(); ++i) {
            Player player = players.get(i);
            ArrayList<Property> properties = player.getProperties();

            json.append("{\n");
            json.append("\"id\": " + player.getId() + ",\n");
            json.append("\"name\": \"" + player.getName() + "\",\n");
            json.append("\"money\": " + player.getMoney() + ",\n");
            json.append("\"currentPosition\": " + player.getCurrentPosition() + ",\n");

            json.append("\"properties\": [");
            for (int j = 0; j < properties.size(); ++j) {
                json.append(properties.get(j).getPosition());
                if (j < properties.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("]\n");
            json.append("}");
            if (i < players.size() - 1) {
                json.append(",\n");
            }
        }
        json.append("\n],\n");

        json.append("\"squares\": [\n");
        for (int i = 0; i < squares.size(); ++i) {
            Square square = squares.get(i);
            json.append("{\n");
            json.append("\"id\": " + square.getId() + ",\n");
            String type = null;
            String details = "{}";
            if (square instanceof Property) {
                Property property = (Property) square;
                type = "P";
                details = String.format("{\n\"name\": \"%s\",\n\"price\": %f,\n\"rent\": %f\n}", property.getName(),
                        property.getPrice(), property.getRent());
            } else if (square instanceof Go) {
                type = "G";
            } else if (square instanceof Chance) {
                type = "C";
            } else if (square instanceof IncomeTax) {
                type = "I";
            } else if (square instanceof FreeParking) {
                type = "F";
            } else if (square instanceof GoJail) {
                type = "J";
            } else if (square instanceof InJailOrJustVisiting) {
                type = "V";
            }

            json.append("\"type\": \"" + type + "\",\n");
            json.append("\"details\": " + details + "\n");

            json.append("}");
            if (i < squares.size() - 1) {
                json.append(",\n");
            }
        }
        json.append("\n]\n");

        // Add additional elements if necessary
        // Example entries for non-property squares
        json.append("}");

        try (FileWriter writer = new FileWriter(filename + ".json")) {
            writer.write(json.toString());
            System.out.println("Game saved successfully to " + filename + ".json");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save game.");
        }
    }

    public boolean loadGame(String filename, int mode) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename.endsWith(".json")?filename:filename + ".json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load game.");
            return false;
        }
    
        String jsonContent = contentBuilder.toString();
        jsonContent = jsonContent.replaceAll("\\s+", "");
        if(mode==0 && jsonContent.startsWith("{\"id\"") || mode==1 && !jsonContent.startsWith("{\"id\"")) return false;
    
        try {
            String squaresStr = jsonContent.split("\"squares\":\\[")[1].split("\\]")[0];
            String[] squareObjects = squaresStr.split("\\},\\{");
            squares.clear(); 
    
            for (String squareObjStr : squareObjects) {
                squareObjStr = squareObjStr.replaceAll("\\{|\\}", ""); 
    
                int squareId = Integer.parseInt(squareObjStr.split("\"id\":")[1].split(",")[0]);
                String type = squareObjStr.split("\"type\":\"")[1].split("\"")[0];
    
                Square square = null;
    
                switch (type) {
                    case "P":
                        String detailsStr = squareObjStr.split("\"details\":")[1];
                        String propertyName = detailsStr.split("\"name\":\"")[1].split("\"")[0];
                        int price = Integer.parseInt(detailsStr.split("\"price\":")[1].split(",")[0]);
                        int rent = Integer.parseInt(detailsStr.split("\"rent\":")[1].split(",")[0]);
                        square = new Property(squareId, propertyName, price, rent);
                        break;
                    case "G":  
                        square = new Go(squareId);
                        break;
                    case "C":  
                        square = new Chance(squareId);
                        break;
                    case "I":  
                        square = new IncomeTax(squareId);
                        break;
                    case "F":  
                        square = new FreeParking(squareId);
                        break;
                    case "J":  
                        square = new GoJail(squareId);
                        break;
                    case "V":  
                        square = new InJailOrJustVisiting(squareId);
                        break;
                }
                squares.add(square);
            }


            if(mode == 1) {
                String gameIdStr = jsonContent.split("\"id\":")[1].split(",")[0];

                String playersStr = jsonContent.split("\"players\":\\[")[1].split("\\],")[0];
                String[] playerObjects = playersStr.split("\\},\\{");

                int gameId = Integer.parseInt(gameIdStr);
                Gameboard.count = gameId;
                players.clear();
                for (String playerObjStr : playerObjects) {
                    playerObjStr = playerObjStr.replaceAll("\\{|\\}", "");

                    int playerId = Integer.parseInt(playerObjStr.split("\"id\":")[1].split(",")[0]);
                    String playerName = playerObjStr.split("\"name\":\"")[1].split("\"")[0];
                    int playerMoney = Integer.parseInt(playerObjStr.split("\"money\":")[1].split(",")[0]);
                    int currentPosition = Integer.parseInt(playerObjStr.split("\"currentPosition\":")[1].split(",")[0]);

                    Player player = new Player(playerId, playerName, playerMoney, currentPosition);

                    String propertiesStr = playerObjStr.split("\"properties\":\\[")[1].split("\\]")[0];
                    String[] propertyPositions = propertiesStr.split(",");
                    for (String propPos : propertyPositions) {
                        int propertyPosition = Integer.parseInt(propPos.trim());
                        Property property = (Property) squares.get(propertyPosition);
                        player.addProperty(property);
                    }

                    players.add(player);
                }
            }
    
            System.out.println("Game loaded successfully from " + filename + ".json");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load game.");
        }
        return true;
    }

    public void displayBoard() {
        System.out.println(boardString);
    }

    public void replaceBlockBySquare(Square square) {
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
