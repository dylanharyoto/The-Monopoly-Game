package main.controller;

import main.model.*;
import main.view.GameboardView;
import main.view.InputOutputView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.UUID;

public class GameboardController {
    private Gameboard gameboard;
    private GameboardView gameboardView;
    public GameboardController(Gameboard gameboard, GameboardView gameboardView) {
        this.gameboard = gameboard;
        this.gameboardView = gameboardView;
    }
    // HELPER FUNCTIONS FOR newGame()
    private int getNumberOfPlayers(int min, int max) {
        String choice = InputOutputView.promptInput(
                "Please enter the number of players (minimum " + min + ", maximum " + max + ")",
                new String[]{"2", "3", "4", "5", "6"}
        );
        return Integer.parseInt(choice);
    }

    private int getNameingOption () {
        String choice = InputOutputView.promptInput(
                """
                Please decide your naming option for players:
                1. Input to name each player
                2. Randomly generate name for each player
                """,
                new String[]{"1", "2"}
        );
        return Integer.parseInt(choice);
    }
    private void displayCurrentPlayers(ArrayList<String> names) {
        InputOutputView.displayMessage("Current players:");
        if (names.isEmpty()) {
            InputOutputView.displayMessage("None");
        } else {
            for (int i = 0; i < names.size(); i++) {
                InputOutputView.displayMessage((i + 1) + ". " + names.get(i));
            }
        }
    }
    private ArrayList<String> collectPlayerNames(int numberOfPlayers) {
        ArrayList<String> names = new ArrayList<>(numberOfPlayers);
        while (names.size() < numberOfPlayers) {
            displayCurrentPlayers(names);
            String name = InputOutputView.promptInput("Please type the name of the " + (names.size() + 1) + "th player (minimum 1, maximum 9 characters)", new String[]{});
            if (name.length() > 10 || name.isEmpty()) {
                InputOutputView.displayMessage("The name must have a minimum of 1 character and a maximum of 9 characters!");
            }
            else if (names.contains(name.trim())) {
                InputOutputView.displayMessage("The name has been used by another player. Please type in a different name!");
            }
            else {
                names.add(name.trim());
            }
        }
        displayCurrentPlayers(names);
        return names;
    }

    private ArrayList<String> generateRandomNames (int numberOfPlayers) {
        ArrayList<String> names = new ArrayList<>(numberOfPlayers);
        while (names.size() < numberOfPlayers) {
            String name = UUID.randomUUID().toString().substring(0, 7); // return a 6-character string
            if (!names.contains(name)) {
                names.add(name);
            }
        }
        displayCurrentPlayers(names);
        return names;
    }
    private boolean chooseAndLoadMap() {
        String curdir = System.getProperty("user.dir");
        while (true) {
            String choice = InputOutputView.promptInput(
                    "Would you like to\n1. Start with default map\n2. Start by loading a map\n3. Restart",
                    new String[]{"1", "2", "3"}
            );
            switch (choice) {
                case "1":
                    if (GameboardManager.loadMap(curdir + "/assets/maps/defaultMap", gameboard)) {
                        return true;
                    }
                    InputOutputView.displayMessage("Default map does not exist!\n");
                    break;
                case "2":
                    String filename = InputOutputView.promptFilename("Please input the JSON filename");
                    if (GameboardManager.loadMap(curdir + "/assets/maps/" + filename, gameboard)) {
                        return true;
                    }
                    InputOutputView.displayMessage("File does not exist!\n");
                    break;
                case "3":
                    return false;
            }
        }
    }
    private void initializePlayers(ArrayList<String> names, int startingMoney) {
        for (int i = 0; i < names.size(); i++) {
            gameboard.addPlayer(new Player(i + 1, names.get(i), startingMoney, 1));
        }
    }
    public void newGame() {
        final int MIN_PLAYERS = 2;
        final int MAX_PLAYERS = 6;
        final int STARTING_MONEY = 1500;
        int numberOfPlayers = getNumberOfPlayers(MIN_PLAYERS, MAX_PLAYERS);
        ArrayList<String> playerNames;
        if (getNameingOption() == 1) {
            playerNames = collectPlayerNames(numberOfPlayers);
        }
        else {
            playerNames = generateRandomNames(numberOfPlayers);
        }

        if (!chooseAndLoadMap()) {
            return;
        }
        initializePlayers(playerNames, STARTING_MONEY);
        startGame();
    }

    // HELPER FUNCTIONS FOR startGame()
    private void checkPlayerStatus() {
        String playerStatusOption = InputOutputView.promptInput(
                """
                Would you like to:
                1. Go back to previous options
                2. Check all players
                3. Check a single player""", new String[]{"1", "2", "3"});

        switch (playerStatusOption) {
            case "1":
                break;
            case "2":
                gameboardView.displayAllPlayers(gameboard.getAllPlayers());
                break;
            case "3":
                String[] playerIdOptions = new String[gameboard.getAllPlayers().size()];
                InputOutputView.displayMessage("Type the player ID (number on the left of the player's name)");
                for (int i = 0; i < gameboard.getAllPlayers().size(); i++) {
                    playerIdOptions[i] = String.valueOf(i + 1);
                    InputOutputView.displayMessage(i + 1 + ". " + gameboard.getAllPlayers().get(i).getName());
                }
                int playerID = Integer.parseInt(InputOutputView.promptInput("", playerIdOptions));
                gameboardView.displayPlayer(gameboard.getPlayerByID(playerID));
                break;
        }
    }
    private void displayGameboardStatus() {
        gameboardView.updateGameboard(gameboard);
        gameboardView.displayGameboard();
    }
    private void displayNextPlayer() {
        Player nextPlayer = gameboard.getNextPlayer();
        gameboardView.displayPlayer(nextPlayer);
    }
    private void saveGame() {
        String curdir = System.getProperty("user.dir");
        String filename = curdir + "/assets/games/" + gameboard.generateGameID() + ".json";
        GameboardManager.saveGame(gameboard, filename);
    }
    private int handlePlayerOptions(Player currentPlayer) {
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
                return 1;
            case "2":
                checkPlayerStatus();
                break;
            case "3":
                displayGameboardStatus();
                break;
            case "4":
                displayNextPlayer();
                break;
            case "5":
                saveGame();
                break;
            case "6":
                return 2;
            default:
                InputOutputView.displayMessage("You must roll the dice (type \"1\") before proceeding!\n");
                break;
        }
        return 0;
    }
    private void handlePlayerMovement(Player currentPlayer, int initialPosition, int currentPosition) {
        int currentGoPosition = gameboard.getGoPosition();
        if (initialPosition == currentPosition) {
            String jailOption = InputOutputView.promptInput("""
            You are currently in jail. Would you like to:
            1. Pay HKD$150 to get out
            2. Stay in jail
            """, new String[]{"1", "2"});
            if ("1".equals(jailOption)) {
                currentPlayer.decreaseMoney(150);
                currentPlayer.setInJailDuration(0);
            }
        } else {
            if (8 >= currentPosition && 13 <= initialPosition && currentPosition != 1) {
                gameboard.getSquareByPosition(currentGoPosition).takeEffect(currentPlayer);
            }
        }
        gameboard.getSquareByPosition(currentPosition).takeEffect(currentPlayer);
    }
    private void checkPlayerBankruptcy(Player currentPlayer) {
        if (currentPlayer.getMoney() < 0) {
            for (Property property : currentPlayer.getProperties()) {
                property.setOwner(null);
            }
            InputOutputView.displayMessage("[UPDATE] " + currentPlayer.getName() + " is out of the game!\n");
            gameboard.removePlayer(currentPlayer);
        }
    }
    public void startGame() {
        for(Square s: gameboard.getAllSquares()) {
            gameboardView.replaceBlockBySquare(s);
        }
        InputOutputView.displayMessage("Welcome to MonoPolyU, the game is starting...");
        while (gameboard.checkGameStatus()) {
            Player currentPlayer = this.gameboard.getPlayerByID(this.gameboard.getCurrentPlayerID());
            int currentPlayerInitialPosition = currentPlayer.getPosition();
            int flag = 0;
            while (flag == 0) {
                flag = handlePlayerOptions(currentPlayer);
            }
            if (flag > 1) {
                break;
            }
            int currentPlayerCurrentPosition = currentPlayer.getPosition();
            handlePlayerMovement(currentPlayer, currentPlayerInitialPosition, currentPlayerCurrentPosition);
            checkPlayerBankruptcy(currentPlayer);
            gameboard.nextPlayer();
        }
        endGame();
    }
    public void endGame() {
        int[] winnersId = gameboard.getWinners();
        if (winnersId.length <= 1) {
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
    public static void designMap() {
        String designOption = InputOutputView.promptInput("""
                Would you like to
                1. Design from the default map
                2. Design from other map""", new String[]{"1", "2"});
        String curdir = System.getProperty("user.dir");
        String filename = "";
        switch (designOption) {
            case "1":
                filename = "defaultMap";
                break;
            case "2":
                filename = InputOutputView.promptFilename("Please input the JSON filename here");
                break;
        }
        String filepath = curdir + "/assets/maps/" + filename;
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
            return;
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
        while(true) {
            InputOutputView.displayAllProperties(properties);
            int choice = Integer.parseInt(InputOutputView.promptInput("""
            Would you like to?
            1. Change the name/price/rent of current properties
            2. Print out the properties
            3. Finish changing""", new String[]{"1", "2", "3"}));
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
                    return;
                }
            }
        } else {
            // if updated and successfully saved, already reture true; if not updated then always return true
            InputOutputView.displayMessage("You did not make any change to the default map!\n");
        }
    }
}
