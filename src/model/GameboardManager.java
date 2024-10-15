package model;

import controller.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameboardManager {
    public static void saveGame(Gameboard gameboard, String filename) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        if (gameboard.getId() != 0) {
            json.append("\"id\": ").append(gameboard.getId()).append(",\n");
        } else {
            json.append("\"id\": ").append(Gameboard.generateId()).append(",\n");
        }
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

        json.append("\"squares\": [\n");
        for (int i = 0; i < gameboard.getTotalSquares(); ++i) {
            Square square = gameboard.getAllSquares().get(i);
            json.append("{\n");
            json.append("\"id\": ").append(square.getId()).append(",\n");
            json.append(square.typeDetailsJson());
            json.append("}");
            if (i < gameboard.getTotalSquares() - 1) {
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
            System.out.println("Failed to save game.");
        }
    }



    public static boolean loadMap (String mapFileName, Gameboard gameboard) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFileName.endsWith(".json")?filename:filename + ".json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the map file: " + mapFileName);
            return false;
        }
        String jsonContent = contentBuilder.toString();
        jsonContent = jsonContent.replaceAll("\\s+", "");

        try {
            String squaresStr = jsonContent.split("\"squares\":\\[")[1].split("\\]")[0];
            String[] squareObjects = squaresStr.split("\\},\\{");
            gameboard.getAllSquares().clear();

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
                gameboard.addSquare(square);
            }

            System.out.println("Map loaded successfully from " + mapFileName + ".json");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to interpret the map: " + mapFileName);
        }

        return true;
    }

    /**
     * @param filename filename is in the format "<>timestamp</>_game.json"
     * @param gameboard gameboard is the instance of gameboard "template" without specified map and players
     * @return true if the loading is successful, otherwise return false
     */
    public static boolean loadGame(String filename, Gameboard gameboard) {
        // mode == 0 means only loadMap
        // mode == 1 means load both map and players from the saved files.


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
        if(!jsonContent.startsWith("{\"gameid\"")) return false;

        try {
            String gameId = jsonContent.split("\"gameid\":")[1].split(",")[0];
            String mapId = jsonContent.split("\"mapid\":")[1].split(",")[0];

            gameboard.setId(gameId);


            if (!loadMap(mapId, gameboard)) return false;

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


            System.out.println("Game loaded successfully from " + filename + ".json");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load game.");
        }

        return true;
    }

}
