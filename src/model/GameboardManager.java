package model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
            System.out.println("Game saved successfully to " + filepath );
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save game.");
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
            System.out.println("Failed to load the map file: " + filepath);
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

            System.out.println("Map loaded successfully from " + filepath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to interpret the map: " + filepath);
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
            System.out.println("Failed to load game.");
            return false;
        }

        String jsonContent = contentBuilder.toString();
        jsonContent = jsonContent.replaceAll("\\s+", "");
        if(!jsonContent.startsWith("{\"gameid\"")) return false;

        try {
            String gameId = jsonContent.split("\"gameid\":")[1].split(",")[0];
            String mapId = jsonContent.split("\"mapid\":")[1].split(",")[0];

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


            System.out.println("Game loaded successfully from " + filepath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load game from" + filepath);
        }

        return true;
    }

}
