package model;


import view.InputOutputView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameboardManager {
    public static void saveGame(Gameboard gameboard, String filepath) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("\"gameid\": ").append(gameboard.getGameID()).append(",\n");
        json.append("\"mapid\": ").append(gameboard.getMapID()).append(",\n");
        json.append("\"players\": [\n");
        for (int i = 0; i < gameboard.getAllPlayers().size(); ++i) {
            Player player = gameboard.getPlayerByID(i + 1);
            ArrayList<Property> properties = player.getProperties();
            json.append("{\n");
            json.append("\"id\": ").append(player.getId()).append(",\n");
            json.append("\"name\": \"").append(player.getName()).append("\",\n");
            json.append("\"money\": ").append(player.getMoney()).append(",\n");
            json.append("\"currentPosition\": ").append(player.getPosition()).append(",\n");
            json.append("\"inJailDuration\": ").append(player.getInJailDuration()).append(",\n");
            json.append("\"properties\": [");
            for (int j = 0; j < properties.size(); ++j) {
                json.append(properties.get(j).getPosition());
                if (j < properties.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("]\n");
            json.append("}");
            if (i < gameboard.getAllPlayers().size() - 1) {
                json.append(",\n");
            }
        }
        json.append("\n],\n");
        json.append("}");
        try (FileWriter writer = new FileWriter((filepath.endsWith(".json") ? filepath : (filepath + ".json")))) {
            writer.write(json.toString());
            InputOutputView.displayMessage("[SUCCESS] The game was successfully saved to " + filepath + "!\n");
        } catch (IOException e) {
            InputOutputView.displayMessage("[FAILURE] The game failed to save to " + filepath + "!\n");
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
        try (BufferedReader reader = new BufferedReader(new FileReader( (filepath.endsWith(".json") ? filepath : (filepath + ".json")) )) ) {
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
        try {
            String mapId = jsonContent.split("\"mapid\":")[1].split(",")[0].replace("\"", "");
            gameboard.setMapID(mapId);
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
            InputOutputView.displayMessage("[SUCCESS] The map was successfully loaded from " + filepath + "!\n");
        } catch (Exception e) {
            InputOutputView.displayMessage("[FAILURE] The map failed to be interpreted from " + filepath);
        }
        return true;
    }
    /**
     * load game and players to the passed reference of gameboard
     * @param filepath filepath is the absolute path of the game file, the filename is in the format "<>timestamp</>_game.json"
     * @param gameboard gameboard is the instance of gameboard "template" without specified map and players
     * @return true if the loading is successful, otherwise return false
     */
    public static boolean loadGame(Gameboard gameboard) {
        String filename = InputOutputView.promptFilename("Please input the JSON filename here");
        filename = filename.endsWith(".json") ? filename : filename + ".json";
        String curdir = System.getProperty("user.dir");
        String filepath = curdir + "/assets/games/" + filename;
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader( (filepath.endsWith(".json") ? filepath : (filepath + ".json")) )) ) {
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
        if(!jsonContent.startsWith("{\"gameid\"")) {
            return false;
        }
        try {
            String gameId = jsonContent.split("\"gameid\":")[1].split(",")[0];
            String mapId = jsonContent.split("\"mapid\":")[1].split(",")[0].replace("\"", "");
            gameboard.setGameID(gameId);
            if (!loadMap(curdir + "/assets/maps/" + mapId, gameboard)) {
                return false;
            }
            String playersStr = jsonContent.split("\"players\":\\[")[1].split("\\],")[0];
            String[] playerObjects = playersStr.split("\\},\\{");
            gameboard.getAllPlayers().clear();
            for (String playerObjStr : playerObjects) {
                playerObjStr = playerObjStr.replaceAll("\\{|\\}", "");
                int playerId = Integer.parseInt(playerObjStr.split("\"id\":")[1].split(",")[0]);
                String playerName = playerObjStr.split("\"name\":\"")[1].split("\"")[0];
                int playerMoney = Integer.parseInt(playerObjStr.split("\"money\":")[1].split(",")[0]);
                int currentPosition = Integer.parseInt(playerObjStr.split("\"currentPosition\":")[1].split(",")[0]);
                int inJailDuration = Integer.parseInt(playerObjStr.split("\"inJailDuration\":")[1].split(",")[0]);
                Player player = new Player(playerId, playerName, playerMoney, currentPosition);
                player.setInJailDuration(inJailDuration);
                String[] propertyList = playerObjStr.split("\"properties\":\\[")[1].split("\\]");
                String propertiesStr = propertyList.length == 0 ? "" : propertyList[0];
                if (!propertiesStr.isEmpty()) {
                    String[] propertyIds = propertiesStr.split(",");
                    for (String propPos : propertyIds) {
                        int propertyPosition = Integer.parseInt(propPos.trim());
                        Property property = (Property) gameboard.getSquareByPosition(propertyPosition);
                        player.addProperty(property);
                    }
                }
                gameboard.addPlayer(player);
            }
            InputOutputView.displayMessage("[SUCCESS] The game was successfully loaded from " + filepath + "!\n");
        } catch (Exception e) {
            InputOutputView.displayMessage("[FAILURE] The game failed to load from " + filepath + "!\n");
        }
        return true;
    }
    public static boolean saveMap (ArrayList<Square> squares, String mapid, String filepath) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("\"mapid\": \"").append(mapid).append("\",\n");
        json.append("\"squares\": [\n");
        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            json.append("{\n");
            json.append("\"id\": \"").append(square.getId()).append("\",\n");
            json.append(square.detailsInJSON());
            json.append("}");
            if (i < squares.size() - 1) {
                json.append(",\n");
            }
        }
        json.append("\n]\n");
        json.append("}");
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(json.toString());
            InputOutputView.displayMessage("[SUCCESS] The map was successfully saved to " + filepath + "!\n");
        } catch (IOException e) {
            InputOutputView.displayMessage("[FAILURE] The game failed to save to " + filepath + "!\n");
        }
        return true;
    }
}
