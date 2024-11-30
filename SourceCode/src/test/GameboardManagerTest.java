package test;
import main.model.*;
import main.view.InputOutputView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameboardManagerTest {
    private Gameboard gameboard;
    private Player player;

    @BeforeEach
    void setUp() {
        gameboard = new Gameboard();
        player = new Player(1, "Alice", 1500, 1);
        gameboard.addPlayer(player);
    }

    @Test
    void testSaveGame_Success() {
        String filepath = "test_game.json";
        GameboardManager.saveGame(gameboard, filepath);

        // Verify that the file was created and contains expected content
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            assertTrue(content.toString().contains("\"players\": ["));
            assertTrue(content.toString().contains("\"name\": \"Alice\""));
        } catch (IOException e) {
            fail("Exception during reading the game file: " + e.getMessage());
        } finally {
            new File(filepath).delete(); // Clean up the test file
        }
    }


    @Test
    void testLoadGame_Success() {
        String curdir = System.getProperty("user.dir");
        String filepath = curdir + "/assets/games/" + "test_game.json";
        gameboard.setMapID("defaultMap");
        gameboard.setGameID("1");
        GameboardManager.saveGame(gameboard, filepath);
        InputOutputView.setInput("test_game");
        Gameboard newGameboard = new Gameboard();
        boolean loaded = GameboardManager.loadGame(newGameboard);

        assertTrue(loaded);
        assertEquals("Alice", newGameboard.getPlayerByID(1).getName());

        new File(filepath).delete(); // Clean up the test file
    }

    @Test
    void testLoadGame_Failure() {
        // Simulate an invalid file scenario
        Gameboard newGameboard = new Gameboard();
        boolean loaded = GameboardManager.loadGame(newGameboard);
        assertFalse(loaded);
    }

    @Test
    void testLoadMap_Success() {
        String mapFilePath = "test_map.json";
        GameboardManager.saveMap(new ArrayList<>(), "testMap", mapFilePath); // Assuming saveMap works similarly

        boolean loaded = GameboardManager.loadMap(mapFilePath, gameboard);
        assertTrue(loaded);

        new File(mapFilePath).delete(); // Clean up the test file
    }

    @Test
    void testSaveMap_Success() {
        ArrayList<Square> squares = new ArrayList<>();
        Square square = new Property(1, "P1", "Park Place", 350, 50);
        squares.add(square);
        String mapFilePath = "test_map.json";

        boolean saved = GameboardManager.saveMap(squares, "testMap", mapFilePath);
        assertTrue(saved);

        // Verify that the file was created and contains expected content
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFilePath))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            assertTrue(content.toString().contains("\"mapid\": \"testMap\""));
        } catch (IOException e) {
            fail("Exception during reading the map file: " + e.getMessage());
        } finally {
            new File(mapFilePath).delete(); // Clean up the test file
        }
    }


}