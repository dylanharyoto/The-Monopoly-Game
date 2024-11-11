package main.model;


import main.view.InputView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * load game and players to the passed reference of gameboard
 * @param filepath filepath is the absolute path of the original game file (if any)
 * @param gameboard gameboard is the instance of gameboard with players and squares
 * @return true if the saving is successful, otherwise return false
 */
public class GameboardManager {
    public static void saveGame(Gameboard gameboard, String filepath) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("\"gameid\": ").append(Gameboard.generateGameID()).append(",\n");
        json.append("\"players\": [\n");
        for (int i = 0; i < gameboard.getTotalPlayers(); ++i) {
            Player player = gameboard.getPlayerById(i + 1);
            ArrayList<Property> properties = player.getProperties();

            json.append("{\n");
            json.append("\"id\": ").append(player.getId()).append(",\n");
            json.append("\"name\": \"").append(player.getName()).append("\",\n");
            json.append("\"money\": ").append(player.getMoney()).append(",\n");
            json.append("\"currentPosition\": ").append(player.getPosition()).append(",\n");

            json.append("\"properties\": [");
            for (int j = 0; j < properties.size(); ++j) {
                json.append(properties.get(j).getPosition());
                if (j < properties.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("]\n");


            json.append("}");
            if (i < gameboard.getTotalPlayers() - 1) {
                json.append(",\n");
            }
        }
        json.append("\n],\n");


        json.append("\"mapid\": " + gameboard.getMapID() + "\n");

        json.append("}");

        try (FileWriter writer = new FileWriter((filepath.endsWith(".json") ? filepath : (filepath + ".json")))) {
            writer.write(json.toString());
            InputView.displayMessage("Game saved successfully to " + filepath );
        } catch (IOException e) {
            e.printStackTrace();
            InputView.displayMessage("Failed to save game.");
        }

    }
    /**
     * load game and players to the passed reference of gameboard
     * @param filepath filepath is the absolute path of the map file
     * @param gameboard gameboard is the instance of gameboard "template" without specified map and players
     * @return true if the loading is successful, otherwise return false
     */
    public static boolean loadMap (String filepath, Gameboard gameboard) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader( (filepath.endsWith(".json") ? filepath : (filepath + ".json")) )) )
        {
            String line;
            while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            InputView.displayMessage("Failed to load the map file: " + filepath);
            return false;
        }
        String jsonContent = contentBuilder.toString();
        jsonContent = jsonContent.replaceAll("\\s+", "");
        String mapId = jsonContent.split("\"mapid\":")[1].split(",")[0];
        gameboard.setMapID(mapId);
        try {
            String squaresStr = jsonContent.split("\"squares\":\\[")[1].split("\\]")[0];
            String[] squareObjects = squaresStr.split("\\},\\{");
            gameboard.getAllSquares().clear();

            int position = 1;
            for (String squareObjStr : squareObjects) {
                squareObjStr = squareObjStr.replaceAll("\\{|\\}", "");

                String idStr = squareObjStr.split("\"id\":\"")[1].split("\"")[0];
                String type = idStr.substring(0, 1);

                Square square = null;

                switch (type) {
                    case "P":
                        String detailsStr = squareObjStr.split("\"details\":")[1];
                        String propertyName = detailsStr.split("\"name\":\"")[1].split("\"")[0];
                        int price = Integer.parseInt(detailsStr.split("\"price\":")[1].split(",")[0]);
                        int rent = Integer.parseInt(detailsStr.split("\"rent\":")[1].split(",")[0]);
                        square = new Property(position, idStr, propertyName, price, rent);
                        break;
                    case "G":
                        square = new Go(position, idStr);
                        gameboard.setGoPosition(position);
                        break;
                    case "C":
                        square = new Chance(position, idStr);
                        break;
                    case "I":
                        square = new IncomeTax(position, idStr);
                        break;
                    case "F":
                        square = new FreeParking(position, idStr);
                        break;
                    case "J":
                        square = new GoJail(position, idStr);
                        break;
                    case "V":
                        square = new InJailOrJustVisiting(position, idStr);
                        break;
                }
                gameboard.addSquare(square);
                position++;
            }

            InputView.displayMessage("Map loaded successfully from " + filepath + ".");
        } catch (Exception e) {
            e.printStackTrace();
            InputView.displayMessage("Failed to interpret the map: " + filepath);
        }

        return true;
    }

    /**
     * load game and players to the passed reference of gameboard
     * @param filepath filepath is the absolute path of the game file, the filename is in the format "<>timestamp</>_game.json"
     * @param gameboard gameboard is the instance of gameboard "template" without specified map and players
     * @return true if the loading is successful, otherwise return false
     */
    public static boolean loadGame(String filepath, Gameboard gameboard) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader( (filepath.endsWith(".json") ? filepath : (filepath + ".json")) )) )
        {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            InputView.displayMessage("Failed to load game.");
            return false;
        }

        String jsonContent = contentBuilder.toString();
        jsonContent = jsonContent.replaceAll("\\s+", "");
        if(!jsonContent.startsWith("{\"gameid\"")) return false;

        try {
            String gameId = jsonContent.split("\"gameid\":")[1].split(",")[0];
            String mapId = jsonContent.split("\"mapid\":\"")[1].split("\"")[0];

            gameboard.setGameID(gameId);
            gameboard.setMapID(mapId);

            String curdir = System.getProperty("user.dir");
            if (!loadMap(curdir + "/assets/maps/" + mapId, gameboard)) return false;

            String playersStr = jsonContent.split("\"players\":\\[")[1].split("\\],")[0];
            String[] playerObjects = playersStr.split("\\},\\{");
            gameboard.getAllPlayers().clear();
            for (String playerObjStr : playerObjects) {
                playerObjStr = playerObjStr.replaceAll("\\{|\\}", "");

                int playerId = Integer.parseInt(playerObjStr.split("\"id\":")[1].split(",")[0]);
                String playerName = playerObjStr.split("\"name\":\"")[1].split("\"")[0];
                int playerMoney = Integer.parseInt(playerObjStr.split("\"money\":")[1].split(",")[0]);
                int currentPosition = Integer.parseInt(playerObjStr.split("\"currentPosition\":")[1].split(",")[0]);

                Player player = new Player(playerId, playerName, playerMoney, currentPosition);

                String propertiesStr = playerObjStr.split("\"properties\":\\[")[1].split("\\]")[0];
                String[] propertyIds = propertiesStr.split(",");
                for (String propPos : propertyIds) {
                    int propertyPosition = Integer.parseInt(propPos.trim());
                    Property property = (Property) gameboard.getSquareByPosition(propertyPosition);
                    player.addProperty(property);
                }

                gameboard.addPlayer(player);
            }


            InputView.displayMessage("Game loaded successfully from " + filepath);
        } catch (Exception e) {
            e.printStackTrace();
            InputView.displayMessage("Failed to load game from" + filepath);
        }

        return true;
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

        ArrayList <Property> properties = new ArrayList<Property>();
        ArrayList <Square> functionalSquares = new ArrayList<Square>();
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
                        properties.add(new Property(position, idStr, propertyName, price, rent));
                        break;
                    case "G":
                        functionalSquares.add(new Go(position, idStr));
                        break;
                    case "C":
                        functionalSquares.add(new Chance(position, idStr));
                        break;
                    case "I":
                        functionalSquares.add(new IncomeTax(position, idStr));
                        break;
                    case "F":
                        functionalSquares.add(new FreeParking(position, idStr));
                        break;
                    case "J":
                        functionalSquares.add(new GoJail(position, idStr));
                        break;
                    case "V":
                        functionalSquares.add(new InJailOrJustVisiting(position, idStr));
                        break;
                }
                position++;

            }

            System.out.println("Properties loaded successfully from " + filepath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to interpret the properties: " + filepath);
        }

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
                    Please specify the index of the property which you want to change here (from 1 to 12, both inclusive)
                    """
                        , new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));

                Property updatingProperty = (Property) properties.get(propertyIndex);
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
                        int newPrice = InputView.updateInteger("Please input the new price of the property, the new name should be a positive integer. The new price is ");
                        updatingProperty.setPrice(newPrice);
                        System.out.println("Successfully update the Price of the property!");
                        break;

                    case 3:
                        int newRent = InputView.updateInteger("Please input the new rent of the property, the new rent should be a positive integer. The new rent is ");
                        updatingProperty.setRent(newRent);
                        System.out.println("Successfully update the Rent of the property!");
                        break;
                }


            }
            else if(choice == 2) {
                InputView.displayAllProperties(properties);
            }
            else {
                break;
            }
        }

        ArrayList <Square> squares = new ArrayList<>();
        squares.addAll(properties);
        squares.addAll(functionalSquares);


        while (true) {

            Scanner scanner = new Scanner(System.in);
            String curdir = System.getProperty("user.dir");

            System.out.println("Please input the json filename here>");

            String filename = scanner.next();
            filename = filename.endsWith(".json") ? filename : filename + ".json";

            if (saveMap(squares, curdir + "/assets/games/" + filename)){
                return true;
            }
        }


    }

    public static boolean saveMap (ArrayList<Square> squares, String filename) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        json.append("\"squares\": [\n");
        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            json.append("{\n");
            json.append("\"id\": ").append(square.getId()).append(",\n");
            json.append(square.typeDetailsJson());
            json.append("}");
            if (i < squares.size() - 1) {
                json.append(",\n");
            }
        }
        json.append("\n]\n");

        json.append("}");

        try (FileWriter writer = new FileWriter(filename + ".json")) {
            writer.write(json.toString());
            System.out.println("Game saved successfully to " + filename + ".json");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save map.");
        }
        return true;
    }
}
