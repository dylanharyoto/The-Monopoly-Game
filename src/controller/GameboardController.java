package controller;

import model.*;
import view.GameboardView;
import view.InputOutputView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameboardController {
    private Gameboard gameboard;
    private GameboardView gameboardView;
    public GameboardController(Gameboard gameboard, GameboardView gameboardView) {
        this.gameboard = gameboard;
        this.gameboardView = gameboardView;
    }
    private void handleAdd(ArrayList<String> names, int numberOfPlayers) {
        String name = InputOutputView.promptInput("Please type the name of the " + (names.size() + 1) + "th player (minimum 1, maximum 9 characters)",new String[]{});
        if (name.length() > 10 || name.isEmpty()) {
            InputOutputView.displayMessage("The name must have a minimum of 1 character and a maximum of 9 characters!");
        } else {
            names.add(name.trim());
        }
    }
    public boolean newGame() {
        ArrayList<String> names = new ArrayList<String>(0);
        String choice = InputOutputView.promptInput("Please enter the number of players (minimum 2, maximum 6)", new String[]{"2", "3", "4", "5", "6"});
        int numberOfPlayers = Integer.parseInt(choice);
        names = new ArrayList<String>(numberOfPlayers);
        while (names.size() < numberOfPlayers) {
            InputOutputView.displayMessage("Current players:");
            if (names.isEmpty()) {
                InputOutputView.displayMessage("None");
            } else {
                for (int i = 0; i < names.size(); i++) {
                    InputOutputView.displayMessage((i + 1) + ". " + names.get(i));
                }
            }
            handleAdd(names, numberOfPlayers);
        }
        InputOutputView.displayMessage("Current players:");
        for (int i = 0; i < names.size(); i++) {
            InputOutputView.displayMessage((i + 1) + ". " + names.get(i));
        }
        while(true){
            choice = InputOutputView.promptInput("Would you like to\n1. Start with default map\n2. Start by loading a map", new String[]{"1", "2"});
            String curdir = System.getProperty("user.dir");
            switch (choice) {
                case "1":
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/defaultMap", gameboard)) {
                        InputOutputView.displayMessage("Default map does not exist!\n");
                        continue;
                    }
                    break;
                case "2":
                    String filename = InputOutputView.promptFilename("Please input the JSON filename");
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/" + filename, gameboard)){
                        InputOutputView.displayMessage("File does not exist!\n");
                        continue;
                    }
                    break;
            }
            break;
        }
        for (int i = 0; i < names.size(); i++) {
            gameboard.addPlayer(new Player(i + 1, names.get(i), 1500, 1));
        }
        startGame();
        return true;
    }
    public void startGame() {
        for(Square s: gameboard.getAllSquares()) gameboardView.replaceBlockBySquare(s);
        InputOutputView.displayMessage("Welcome to MonoPolyU, the game is starting...");
        Player currentPlayer;
        while (gameboard.checkGameStatus()) {
            currentPlayer = this.gameboard.getPlayerByID(this.gameboard.getCurrentPlayerID());
            int currentPlayerInitialPosition = currentPlayer.getPosition();
            boolean proceed = false;
            while(!proceed) {
                String option = InputOutputView.promptInput(currentPlayer.getName() +
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
                        String playerStatusOption = InputOutputView.promptInput(
                    """
                        Would you like to:
                        1. Go back to previous options
                        2. Check all players
                        3. Check a single player""", new String[]{"1", "2", "3"});
                        switch (playerStatusOption) {
                            case "1" :
                                break;
                            case "2" :
                                gameboardView.displayAllPlayers(gameboard.getAllPlayers());
                                break;
                            case "3" :
                                int playerId = InputOutputView.promptGetPlayerByID(gameboard.getAllPlayers(), gameboard.getAllPlayers().size());
                                gameboardView.displayPlayer(gameboard.getPlayerByID(playerId));
                                break;
                        }
                        break;
                    case "3":
                        this.gameboardView.updateGameboard(gameboard);
                        this.gameboardView.displayGameboard();
                        break;
                    case "4":
                        Player nextPlayer = gameboard.getNextPlayer();
                        gameboardView.displayPlayer(nextPlayer);
                        break;
                    case "5":
                        String curdir = System.getProperty("user.dir");
                        String filename =  curdir + "/assets/games/" + gameboard.generateGameID() + ".json";
                        GameboardManager.saveGame(gameboard, filename);
                        break;
                    case "6":
                        return;
                    default:
                        InputOutputView.displayMessage("You must roll the dice (type \"1\") before proceeding!\n");
                        break;
                }
            }
            int currentPlayerCurrentPosition = currentPlayer.getPosition();
            int currentGoPosition = gameboard.getGoPosition();
            if (currentPlayerInitialPosition == currentPlayerCurrentPosition) {
                String jailOption = InputOutputView.promptInput("""
                You are currently in jail. Would you like to:
                1. Pay HKD$150 to get out
                2. Stay in jail
                """, new String[]{"1", "2"});
                switch (jailOption) {
                    case "1":
                        currentPlayer.decreaseMoney(150);
                        currentPlayer.setInJailDuration(0);
                        break;
                    case "2":
                        break;
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
                InputOutputView.displayMessage("[UPDATE] " + currentPlayer.getName() + " is out of the game!\n");
                gameboard.removePlayer(currentPlayer);
            }
            gameboard.nextPlayer();
        }
        endGame();
    }
    public void endGame() {
        int[] winnersId = gameboard.getWinners();
        if (winnersId.length == 1) {
            InputOutputView.displayMessage("[UPDATE] Game has ended! The winner is " + gameboard.getPlayerByID(winnersId[0]).getName());
        } else {
            StringBuilder winnerNames = new StringBuilder("[UPDATE] Game has ended! The winners are ");
            for (int i = 0; i < winnersId.length; i++) {
                winnerNames.append(gameboard.getPlayerByID(winnersId[i]).getName());
                if (i != winnersId.length - 1) {
                    winnerNames.append(", ");
                }
            }
            InputOutputView.displayMessage(winnerNames.toString());
        }
    }
    /** change the rent or properties as the requirements of the oesigner, and save it as a new map file
     * @param filepath  filepath is the absolute path of the default map file, the filename is in the format "<>timestamp</>_map.json"
     * @param squares squares is an arraylist of loaded default squares
     * @return if the design (i.e., change the rent of properties and save of the new map file) is successful, otherwise return false
     */
    public static boolean designMap(String filepath) {
        // read from the default map file to load the original properties first
        StringBuilder contentBuilder = new StringBuilder();
        filepath = (filepath.endsWith(".json") ? filepath : (filepath + ".json"));
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath)))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }
        } catch (IOException e) {
            InputOutputView.displayMessage("[FAILURE] The map failed to load from " + filepath + "!\n");
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
            InputOutputView.displayMessage("[SUCCESS] The properties were successfully loaded from " + filepath + "!\n");
        } catch (Exception e) {
            InputOutputView.displayMessage("[FAILURE] The properties failed to be interpreted from " + filepath + "!\n");
        }
        boolean mapUpdated = false;
        // Cooperate with View part to prompt the designer to update the properties accordingly
        InputOutputView.displayAllProperties(properties);
        while(true) {
            int choice = Integer.parseInt(InputOutputView.promptInput("""
            Would you like to change the name/price/rent of current properties given indices?
            1. Need to change
            2. Print out the properties again
            3. No need to change""", new String[]{"1", "2", "3"}));
            if(choice == 1) {
                int propertyIndex = Integer.parseInt(InputOutputView.promptInput("""
                Please type the index of the property which you want to change (from 0 to 11, both inclusive)""", new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}));
                Property updatingProperty = (Property) squares.get(propertyIndices.get(propertyIndex));
                InputOutputView.displayProperty(updatingProperty);
                int attributeOption = Integer.parseInt(InputOutputView.promptInput("""
                Which attribute would you like to change for this property?\s
                1. Name
                2. Price
                3. Rent""", new String[]{"1", "2", "3"}));
                switch (attributeOption) {
                    case 1:
                        String newName = InputOutputView.promptString("Please input the new name of the property (0-9 long, characters)");
                        updatingProperty.setName(newName);
                        InputOutputView.displayMessage("[SUCCESS] Successfully updated the name of the property!\n");
                        break;
                    case 2:
                        int newPrice = InputOutputView.promptInteger("Please input the new price of the property, (positive integer)");
                        updatingProperty.setPrice(newPrice);
                        InputOutputView.displayMessage("[SUCCESS] Successfully updated the price of the property!\n");
                        break;
                    case 3:
                        int newRent = InputOutputView.promptInteger("Please input the new rent of the property (positive integer)");
                        updatingProperty.setRent(newRent);
                        InputOutputView.displayMessage("[SUCCESS] Successfully updated the rent of the property!\n");
                        break;
                }
                mapUpdated = true;
                properties.set(propertyIndex, updatingProperty);
            } else if(choice == 2) {
                InputOutputView.displayAllProperties(properties);
            } else {
                break;
            }
        }
        if (mapUpdated) {
            while (true) {
                String curdir = System.getProperty("user.dir");
                String saveOption = InputOutputView.promptInput("""
                Would you like to
                1. Overwrite the current map
                2. Create a new map""", new String[]{"1", "2"});
                String mapid = "";
                switch (saveOption) {
                    case "1":
                        mapid = filepath.split("/")[filepath.split("/").length - 1];
                        break;
                    case "2":
                        mapid = InputOutputView.promptFilename("Please input the name of the new map");
                        mapid = mapid.endsWith(".json") ? mapid : mapid + ".json";
                        break;
                }
                if (GameboardManager.saveMap(squares, mapid, curdir + "/assets/maps/" + mapid)){
                    InputOutputView.displayMessage("[SUCCESS] Thanks for designing a new map!\n");
                    return true;
                }
            }
        } else {
            // if updated and successfully saved, already reture true; if not updated then always return true
            InputOutputView.displayMessage("You did not make any change to the default map!\n");
            return true;
        }
    }
}
