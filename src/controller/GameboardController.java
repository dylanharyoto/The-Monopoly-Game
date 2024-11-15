package controller;

import model.*;
import view.GameboardView;
import view.InputView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameboardController {
    private Gameboard gameboard;
    private GameboardView gameboardView;
    public GameboardController(Gameboard gameboard, GameboardView gameboardView) {
        this.gameboard = gameboard;
        this.gameboardView = gameboardView;
    }

    private static void handleAdd(ArrayList<String> names, int playerNumber) {
        String name = InputView.inputPrompt("",new String[]{});
        if (name.length() > 10 || name.isEmpty()) {
            InputView.displayMessage("One of the name length isn't permitted.");
        } else if (names.size() == playerNumber) {
            InputView.displayMessage("Slots are taken, player '" + name.trim() + "' is ignored.");
        } else {
            names.add(name.trim());
        }
    }

    public boolean newGame() {
        boolean proceed = false;
        ArrayList<String> names = new ArrayList<String>(0);
        String choice = InputView.inputPrompt("Please enter the number of players (minimum 2, maximum 6)", new String[]{"2", "3", "4", "5", "6"});
        int playerNumber = Integer.parseInt(choice);
        if (playerNumber >= 2 && playerNumber <= 6) {
            names = new ArrayList<String>(playerNumber);
            while (true) {
                InputView.displayMessage("Current players:");
                if (names.isEmpty()) {
                    InputView.displayMessage("None");
                } else {
                    for (int i = 0; i < names.size(); i++) {
                        InputView.displayMessage((i + 1) + ". " + names.get(i));
                    }
                }
                String option = InputView.inputPrompt("Would you like to \n1. Add more players\n2. Start the game", new String[]{"1", "2"});
                if (option.equals("1")) {
                    handleAdd(names, playerNumber);
                } else if (option.equals("2")) {
                    if (names.size() != playerNumber) {
                        InputView.displayMessage("Oops...There's still an empty slot.\n");
                    } else {
                        break;
                    }
                }
            }
            while(true){
                choice = InputView.inputPrompt("Would you like to\n1. Start with default map\n2. Start by loading a map", new String[]{"1", "2"});
                String curdir = System.getProperty("user.dir");
                if(choice.equals("1")) {
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/defaultMap", gameboard))
                        continue;
                } else if(choice.equals("2")) {
                    String filename = InputView.promptFilename("Please input the json filename");
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/" + filename, gameboard)){
                        InputView.displayMessage("File not exist!");
                        continue;
                    }
                }
                break;
            }
        } else {
            InputView.displayMessage("This is not a valid number of players!");
        }
        for (int i = 0; i < names.size(); i++) {
            gameboard.addPlayer(new Player(i+1, names.get(i), 1500, 1));
        }
        startGame();
        return true;
    }
    public void startGame() {
        for(Square s: gameboard.getAllSquares()) gameboardView.replaceBlockBySquare(s);
        InputView.displayMessage("Welcome to MonoPolyU, the game is starting...");
        Player currentPlayer;
        while (gameboard.checkGameStatus()) {
            currentPlayer = this.gameboard.getPlayerById(this.gameboard.getCurrentPlayerId());
            int currentPlayerInitialPosition = currentPlayer.getPosition();
            boolean proceed = false;
            while(!proceed) {
                String option = InputView.inputPrompt(currentPlayer.getName() +
                        """
                        's turn. Would you like to:
                        1. Roll the dice (required to proceed)
                        2. Check player(s) status
                        3. Print the current gameboard status
                        4. Check the next player
                        5. Save the current game
                        6. Close the current game""", new String[]{"1", "2", "3", "4", "5", "6"});
                switch (option) {
                    case "1":
                        currentPlayer.rollDice();
                        proceed = true;
                        break;
                    case "2":
                        String playerStatusOption = InputView.inputPrompt(
                    """
                        Would you like to:
                        1. Go back to previous options
                        2. Check all players
                        3. Check a single player
                        """,
                        new String[]{"1", "2", "3"});
                        switch (playerStatusOption) {
                            case "1" :
                                break;
                            case "2" :
                                gameboardView.displayPlayers(gameboard.getAllPlayers());
                                break;
                            case "3" :
                                int playerId = InputView.promptGetPlayer(gameboard.getAllPlayers(), gameboard.getTotalPlayers());
                                gameboardView.displayPlayer(gameboard.getPlayerById(playerId));
                                break;
                        }
                        break;
                    case "3":
                        this.gameboardView.updateGameboard(gameboard);
                        this.gameboardView.displayGameboard();
                        break;
                    case "4":
                        int nextPlayerId = gameboard.getNextPlayerId();
                        gameboardView.displayPlayer(gameboard.getPlayerById(nextPlayerId));
                        break;
                    case "5":
                        String curdir = System.getProperty("user.dir");
                        String filename =  curdir + "/assets/games/" + gameboard.generateGameID() + ".json";
                        GameboardManager.saveGame(gameboard, filename);
                        break;
                    case "6":
                        return;
                    default:
                        InputView.displayMessage("You must roll the dice (type \"1\") before proceeding.");
                        break;
                }
            }
            int currentPlayerCurrentPosition = currentPlayer.getPosition();
            int currentGoPosition = gameboard.getGoPosition();
            if (currentPlayerInitialPosition == currentPlayerCurrentPosition) {
                String jailOption = InputView.inputPrompt("""
                    You are currently in jail. Would you like to:
                    1. Pay HKD$150 to get out
                    2. Stay in jail
                    """, new String[]{"1", "2"});
                if (jailOption.equals("1")) {
                    currentPlayer.decreaseMoney(150);
                    currentPlayer.setInJailDuration(0);
                }
            } else {
                if (8 >= currentPlayerCurrentPosition && 13 <= currentPlayerInitialPosition && currentPlayerCurrentPosition != 1) {
                    gameboard.getSquareByPosition(currentGoPosition).takeEffect(currentPlayer);
                }
            }
            gameboard.getSquareByPosition(currentPlayerCurrentPosition).takeEffect(currentPlayer);
            if (currentPlayer.getMoney() < 0) {
                for (Property property : currentPlayer.getProperties()) {
                    property.setOwner(null);
                }
                InputView.displayMessage(currentPlayer.getName() + " is out of the game.");
                gameboard.removePlayer(currentPlayer);
            }
            gameboard.nextPlayer();
        }
        endGame();
    }

    /** change the rent or properties as the requirements of the oesigner, and save it as a new map file
     * @param filepath  filepath is the absolute path of the default map file, the filename is in the format "<>timestamp</>_map.json"
     * @param squares squares is an arraylist of loaded default squares
     * @return if the design (i.e., change the rent of properties and save of the new map file) is successful, otherwise return false
     */
    public static boolean designMap (String filepath) {

        // read from the default map file to load the original properties first
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader( (filepath.endsWith(".json") ? filepath : (filepath + ".json")) )) )
        {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the map file: " + filepath);
            return false;
        }
        String jsonContent = contentBuilder.toString();
        jsonContent = jsonContent.replaceAll("\\s+", "");

        ArrayList <Square> squares = new ArrayList<Square>();
        ArrayList <Property> properties = new ArrayList<Property>();
        ArrayList <Integer> propertyIndices = new ArrayList<Integer>();
        try {
            String squaresStr = jsonContent.split("\"squares\":\\[")[1].split("\\]")[0];
            String[] squareObjects = squaresStr.split("\\},\\{");


            int position = 1;
            for (String squareObjStr : squareObjects) {
                squareObjStr = squareObjStr.replaceAll("\\{|\\}", "");
                String idStr = squareObjStr.split("\"id\":\"")[1].split("\"")[0];
                String type = idStr.substring(0, 1);


                switch (type) {
                    case "P":
                        String detailsStr = squareObjStr.split("\"details\":")[1];
                        String propertyName = detailsStr.split("\"name\":\"")[1].split("\"")[0];
                        int price = Integer.parseInt(detailsStr.split("\"price\":")[1].split(",")[0]);
                        int rent = Integer.parseInt(detailsStr.split("\"rent\":")[1].split(",")[0]);
                        squares.add(new Property(position, idStr, propertyName, price, rent));
                        properties.add(new Property(position, idStr, propertyName, price, rent));
                        propertyIndices.add(position-1);
                        break;
                    case "G":
                        squares.add(new Go(position, idStr));
                        break;
                    case "C":
                        squares.add(new Chance(position, idStr));
                        break;
                    case "I":
                        squares.add(new IncomeTax(position, idStr));
                        break;
                    case "F":
                        squares.add(new FreeParking(position, idStr));
                        break;
                    case "J":
                        squares.add(new GoJail(position, idStr));
                        break;
                    case "V":
                        squares.add(new InJailOrJustVisiting(position, idStr));
                        break;
                }
                position++;

            }

            System.out.println("Properties loaded successfully from " + filepath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to interpret the properties: " + filepath);
        }


        boolean mapUpdated = false;
        // Cooperate with View part to prompt the designer to update the properties accordingly
        InputView.displayAllProperties(properties);
        while(true) {
            int choice = Integer.parseInt(InputView.inputPrompt("""
                    
                    Would you like to change the name, price, or rent of current properties given indices?
                        1. Need to change
                        2. Print out the properties again
                        3. No need to change now
                    """
                    , new String[]{"1", "2", "3"}));
            if(choice == 1) {
                int propertyIndex = Integer.parseInt(InputView.inputPrompt("""
                    Please specify the index of the property which you want to change here (from 0 to 11, both inclusive)
                    """
                        , new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}));

                Property updatingProperty = (Property) squares.get(propertyIndices.get(propertyIndex));
                InputView.displayProperty(updatingProperty);

                int attributeOption = Integer.parseInt(InputView.inputPrompt("""
                    Which attribute you would like to change for this property? 
                        1. Name
                        2. Price
                        3. Rent
                    """
                        , new String[]{"1", "2", "3"}));


                switch (attributeOption) {
                    case 1:
                        String newName = InputView.updateName("Please input the new name of the property, the new name should only contain 0-9 and English letters. The new name is ");
                        updatingProperty.setName(newName);
                        System.out.println("Successfully update the Name of the property!");
                        break;
                    case 2:
                        int newPrice = InputView.updateInteger("Please input the new price of the property, the new price should be a positive integer. The new price is ");
                        updatingProperty.setPrice(newPrice);
                        System.out.println("Successfully update the Price of the property!");
                        break;

                    case 3:
                        int newRent = InputView.updateInteger("Please input the new rent of the property, the new rent should be a positive integer. The new rent is ");
                        updatingProperty.setRent(newRent);
                        System.out.println("Successfully update the Rent of the property!");
                        break;
                }

                mapUpdated = true;
                properties.set(propertyIndex, updatingProperty);
            }
            else if(choice == 2) {
                InputView.displayAllProperties(properties);
            }
            else {
                break;
            }
        }



        if (mapUpdated) {
            while (true) {

                Scanner scanner = new Scanner(System.in);
                String curdir = System.getProperty("user.dir");

                System.out.println("Please input the new map name here>");

                String mapid = scanner.next();
                mapid = mapid.replace(".json", "");

                if (GameboardManager.saveMap(squares, mapid, curdir + "/assets/maps/" + mapid + ".json")){
                    InputView.displayMessage("Thanks for designing a new map!");
                    return true;
                }
            }
        }
        else {
            // if updated and successfully saved, already reture true; if not updated then always return true
            InputView.displayMessage("You didn't make any change to the default map.");
            return true;
        }

    }

    public void endGame() {
        int[] winnersId = gameboard.getWinners();

        if (winnersId.length == 1) {
            InputView.displayMessage("Game Finished! The winner is " + gameboard.getPlayerById(winnersId[0]).getName());
        } else {
            StringBuilder winnerNames = new StringBuilder("Game Finished! The winners are ");
            for (int i = 0; i < winnersId.length; i++) {
                winnerNames.append(gameboard.getPlayerById(winnersId[i]).getName());
                if (i != winnersId.length - 1) {
                    winnerNames.append(", ");
                }
            }
            InputView.displayMessage(winnerNames.toString());
        }
    }
}
