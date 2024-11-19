package test.controller;

import main.controller.GameboardController;
import main.model.*;
import main.view.GameboardView;
import main.view.InputOutputView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GameboardControllerTest {
    private Gameboard gameboard;
    private GameboardView gameboardView;
    private GameboardController gameboardController;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        gameboard = new Gameboard();
        gameboardView = new GameboardView(); // Use a real instance for testing
        gameboardController = new GameboardController(gameboard, gameboardView);
        System.setOut(new PrintStream(outputStreamCaptor)); // Redirect output to capture it
    }

    @Test
    void testNewGame_SuccessfulFlow() {
        // Simulate user inputs
        InputOutputView.setInput("3\nAlice\nBob\nCharlie\n1\n6"); // 3 players, names and default map

        // Start the new game
        gameboardController.newGame();

        // Verify that players were initialized correctly
        assertEquals(3, gameboard.getAllPlayers().size());
        assertEquals("Alice", gameboard.getPlayerByID(1).getName());
        assertEquals("Bob", gameboard.getPlayerByID(2).getName());
        assertEquals("Charlie", gameboard.getPlayerByID(3).getName());
    }

    @Test
    void testNewGame_InvalidPlayerName() {
        // Simulate user inputs: 2 players, invalid name for the second player
        InputOutputView.setInput("2\nAlice\n\nBob\n1"); // Second name is empty

        // Start the new game
        gameboardController.newGame();

        // Expect only valid players to be added
        assertEquals(2, gameboard.getAllPlayers().size());
        assertEquals("Alice", gameboard.getPlayerByID(1).getName());
        assertEquals("Bob", gameboard.getPlayerByID(2).getName());
    }

    @Test
    void testChooseAndLoadMap_FileNotFound() {
        // Simulate user inputs: choose to load a map but provide an invalid filename
        InputOutputView.setInput("2\n\na\nb\n2\ninvalid_map.json\n3"); // Invalid map file

        // Start the new game
        gameboardController.newGame();

        // Check for expected console output
        String expectedOutput = "File does not exist!"; // Adjust this based on your actual error message
        assertTrue(outputStreamCaptor.toString().trim().contains(expectedOutput));
    }

    @Test
    void testNewGame_ValidMapSelection() {
        // Simulate user inputs: valid map selection
        InputOutputView.setInput("2\nAlice\nBob\n1"); // 2 players and choose map 1

        // Start the new game
        gameboardController.newGame();

        // Verify that the map was loaded correctly (this requires a specific implementation of your Gameboard)
        // Add relevant assertions related to the map if applicable
        // assertEquals(expectedMapState, gameboard.getMap());
    }

    @Test
    void testEndGame_SingleWinner() {
        Player alice = new Player(1, "Alice", 0, 1);
        alice.setStatus(false);
        gameboard.addPlayer(alice); // Alice bankrupt
        Player bob = new Player(2, "Bob", 0, 1);
        bob.setStatus(false);
        gameboard.addPlayer(bob); // Bob bankrupt
        gameboard.addPlayer(new Player(3, "Charlie", 1500, 1)); // Charlie still in the game

        gameboardController.endGame();
        // Check the output for the winner message
        String expectedOutput = "[UPDATE] Game has ended! The winner is Charlie";
        assertTrue(outputStreamCaptor.toString().trim().contains(expectedOutput));
    }

    @Test
    void testEndGame_MultipleWinners() {
        // Setup players
        Player alice = new Player(1, "Alice", 0, 1);
        alice.setStatus(false);
        gameboard.addPlayer(alice); // Alice bankrupt
        gameboard.addPlayer(new Player(2, "Bob", 1, 1)); // Bob alive
        gameboard.addPlayer(new Player(3, "Charlie", 1500, 1)); // Charlie still in the game

        // Call endGame to finalize the game
        gameboardController.endGame();

        // Check the output for the multiple winners message
        String expectedOutput = "[UPDATE] Game has ended! The players still in the game are Bob, Charlie";
        assertTrue(outputStreamCaptor.toString().trim().contains(expectedOutput));
    }

    @Test
    void testNewGame_OptionSelection() {
        // Simulate user inputs for all options
        String inputs = "2\nAlice\nBob\n1\n6"; // Choose to load a map, player names, then select a map option
        InputOutputView.setInput(inputs);

        // Start the new game
        gameboardController.newGame();

        // Verify that the game was set up correctly by checking player names and states
        assertEquals(2, gameboard.getAllPlayers().size());
        assertEquals("Alice", gameboard.getPlayerByID(1).getName());
        assertEquals("Bob", gameboard.getPlayerByID(2).getName());
    }

    @Test
    void testDesignMap_DefaultMap() {
        // Simulate user inputs for designing from the default map
        InputOutputView.setInput("1\n3\n4"); // Choose option 1 for default map

        // Call the designMap method indirectly through a public method that triggers it
        GameboardController.designMap();

        // Check the output for success message
        assertTrue(outputStreamCaptor.toString().contains("[SUCCESS] The properties were successfully loaded from"));
    }

    @Test
    void testDesignMap_CustomMap() {
        // Create a temporary valid map JSON file for testing
        String testMapFilePath = "assets/maps/testMap.json";
        try {

            // Simulate user inputs for designing from a custom map
            InputOutputView.setInput("2\nmap2\n3\n4"); // Choose option 2 and provide filename

            // Call the designMap method
            GameboardController.designMap();

            // Check the output for success message
            assertTrue(outputStreamCaptor.toString().contains("[SUCCESS] The properties were successfully loaded from"));
        } catch (Exception e) {
            fail("Setup for custom map test failed: " + e.getMessage());
        } finally {
            // Clean up the test file
            try {
                Files.deleteIfExists(Paths.get(testMapFilePath));
            } catch (Exception ignored) {}
        }
    }

    @Test
    void testDesignMap_InvalidFilename() {
        // Simulate user inputs for designing from an invalid map
        InputOutputView.setInput("2\ninvalid_map\n4"); // Invalid map file

        // Call the designMap method
        GameboardController.designMap();

        // Check the output for failure message
        assertTrue(outputStreamCaptor.toString().contains("[FAILURE] The map failed to load from"));
    }

    @Test
    void testUpdatePropertyName() {

        // Simulate user input to update a property
        InputOutputView.setInput("1\n1\n0\n1\nNewName\n3\n1\n4"); // Change name of property at index 0
        GameboardController.designMap();

        // Check the output for success message
        assertTrue(outputStreamCaptor.toString().contains("[SUCCESS] Successfully updated the name of the property!"));
        InputOutputView.setInput("1\n1\n0\n1\nCentral\n3\n1\n4"); // Change name of property at index 0
        GameboardController.designMap();
    }

    @Test
    void testUpdatePropertyPrice() {

        // Simulate user input to update a property
        InputOutputView.setInput("1\n1\n0\n2\n200\n3\n1\n4"); // Change name of property at index 0
        GameboardController.designMap();

        // Check the output for success message
        assertTrue(outputStreamCaptor.toString().contains("0. Central\n" +
                "|---Price: 200 | Rent: 90"));
        InputOutputView.setInput("1\n1\n0\n2\n800\n3\n1\n4"); // Change name of property at index 0
        GameboardController.designMap();
    }

    @Test
    void testUpdatePropertyRent() {

        // Simulate user input to update a property
        InputOutputView.setInput("1\n1\n0\n3\n50\n3\n1\n4"); // Change name of property at index 0
        GameboardController.designMap();

        // Check the output for success message
        assertTrue(outputStreamCaptor.toString().contains("0. Central\n" +
                "|---Price: 800 | Rent: 50"));
        InputOutputView.setInput("1\n1\n0\n3\n90\n3\n1\n4"); // Change name of property at index 0
        GameboardController.designMap();
    }

    @Test
    void testDisplayProperties() {

        // Simulate user input to display properties
        InputOutputView.setInput("1\n3\n4"); // Print out properties and finish changing
        GameboardController.designMap();

        // Check the output for property listings
        assertTrue(outputStreamCaptor.toString().contains("1. WanChai\n" +
                "|---Price: 700 | Rent: 65\n" +
                "2. Stanley\n" +
                "|---Price: 600 | Rent: 60"));
    }

    @Test
    void testDisplayBoard() {
        InputOutputView.setInput("2\nBob\nCharlie\n1\n3\n6");

        gameboardController.newGame();
        assertTrue(outputStreamCaptor.toString().contains("MONOPOLY"));
    }

    @Test
    void testSaveMap() {

        // Simulate user input for saving the map
        InputOutputView.setInput("1\n1\n0\n1\na\n3\n2\nnewMap\n4"); // Overwrite current map or create a new one
        GameboardController.designMap();

        // Check the output for success message after saving
        assertTrue(outputStreamCaptor.toString().contains("[SUCCESS] Thanks for designing a new map!"));
    }

    @Test
    void testNewGame() {
        // Simulate player input for a new game
        InputOutputView.setInput("3\nAlice\nBob\nCharlie\n1\n6"); // 3 players, names, and choose default map

        // Start a new game
        gameboardController.newGame();

        // Verify that players are added correctly
        assertEquals(3, gameboard.getAllPlayers().size());
        assertEquals("Alice", gameboard.getPlayerByID(1).getName());
        assertEquals("Bob", gameboard.getPlayerByID(2).getName());
        assertEquals("Charlie", gameboard.getPlayerByID(3).getName());
    }

    @Test
    void testCheckPlayerStatus_AllPlayers() {
        // Setup players
        gameboard.addPlayer(new Player(1, "Alice", 1500, 1));
        gameboard.addPlayer(new Player(2, "Bob", 1300, 1));

        // Simulate user input to check all players
        InputOutputView.setInput("2\n2\n6"); // Choose option 2 to check all players

        // Call public method that indirectly calls checkPlayerStatus
        gameboardController.startGame(); // Assuming this calls checkPlayerStatus somewhere

        // Verify that player information is displayed
        assertTrue(outputStreamCaptor.toString().contains("Alice"));
        assertTrue(outputStreamCaptor.toString().contains("Bob"));
    }

    @Test
    void testCheckPlayerStatus_SinglePlayer() {
        // Setup players
        gameboard.addPlayer(new Player(1, "Alice", 1500, 1));
        gameboard.addPlayer(new Player(2, "Bob", 1300, 1));

        // Simulate user input to check a single player
        InputOutputView.setInput("2\n3\n1\n6"); // Choose option 3 and select player 1

        // Call public method that indirectly calls checkPlayerStatus
        gameboardController.startGame(); // Assuming this calls checkPlayerStatus somewhere

        // Verify that the specific player's information is displayed
        assertTrue(outputStreamCaptor.toString().contains("Alice"));
    }



}