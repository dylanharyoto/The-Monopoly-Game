package test;
import main.model.*;
import main.view.GameboardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameboardViewTest {
    private GameboardView gameboardView;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        gameboardView = new GameboardView();
        System.setOut(new PrintStream(outputStreamCaptor)); // Redirect output to capture it
    }


    @Test
    void testReplaceBlockBySquare_Property() {
        Property property = new Property(1, "P1", "Park Place", 350, 50);
        gameboardView.replaceBlockBySquare(property);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that the gameboardString contains the property name and price
        String expectedOutput = "Park Place";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    void testReplaceBlockBySquare_Go() {
        Go goSquare = new Go(1, "GO");
        gameboardView.replaceBlockBySquare(goSquare);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that the gameboardString contains "GO!"
        String expectedOutput = "GO!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    void testReplaceBlockBySquare_Chance() {
        Chance chanceSquare = new Chance(1, "CHANCE");
        gameboardView.replaceBlockBySquare(chanceSquare);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that the gameboardString contains "?CHANCE?"
        String expectedOutput = "?CHANCE?";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    void testReplaceBlockBySquare_GoJail() {
        GoJail goJailSquare = new GoJail(1, "GO_JAIL");
        gameboardView.replaceBlockBySquare(goJailSquare);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that the gameboardString contains "GO TO JAIL"
        String expectedOutput = "GO TO JAIL ";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    void testReplaceBlockBySquare_IncomeTax() {
        IncomeTax incomeTaxSquare = new IncomeTax(1, "INCOME_TAX");
        gameboardView.replaceBlockBySquare(incomeTaxSquare);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that the gameboardString contains "INCOME TAX" and "PAY 10%"
        String expectedOutput1 = "   INCOME TAX  ";
        String expectedOutput2 = "  PAY 10%   ";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput1));
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput2));
    }

    @Test
    void testReplaceBlockBySquare_FreeParking() {
        FreeParking freeParkingSquare = new FreeParking(1, "FREE_PARKING");
        gameboardView.replaceBlockBySquare(freeParkingSquare);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that the gameboardString contains "FREE PARKING"
        String expectedOutput = "FREE PARKING";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    void testReplaceBlockBySquare_InJailOrJustVisiting() {
        InJailOrJustVisiting square = new InJailOrJustVisiting(1, "IN_JAIL");
        gameboardView.replaceBlockBySquare(square);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that the gameboardString contains "IN JAIL" and "JUST VISITING"
        String expectedOutput1 = "    IN JAIL ";
        String expectedOutput2 = "   JUST VISITING   ";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput1));
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput2));
    }

    @Test
    void testDisplayAllPlayers_NoPlayers() {
        // Test when there are no players
        gameboardView.displayAllPlayers(new ArrayList<>());

        // Verify that the correct message is displayed
        assertEquals("No players to display.", outputStreamCaptor.toString().trim());
    }

    @Test
    void testDisplayAllPlayers_WithPlayers() {
        // Setup players
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, "Alice", 1500, 1));
        players.add(new Player(2, "Bob", 1300, 2));

        // Display players
        gameboardView.displayAllPlayers(players);

        // Verify that player information is displayed
        assertTrue(outputStreamCaptor.toString().contains("Alice --> P1"));
        assertTrue(outputStreamCaptor.toString().contains("|---Money: HKD 1500"));
        assertTrue(outputStreamCaptor.toString().contains("Bob --> P2"));
        assertTrue(outputStreamCaptor.toString().contains("|---Money: HKD 1300"));
    }

    @Test
    void testDisplayPlayer_NullPlayer() {
        // Test displaying a null player
        gameboardView.displayPlayer(null);

        // Verify that the correct message is displayed
        assertEquals("No player to display.", outputStreamCaptor.toString().trim());
    }

    @Test
    void testDisplayPlayer_WithPlayer() {
        // Setup a player
        Player player = new Player(1, "Alice", 1500, 1);
        player.addProperty(new Property(1, "1", "Park Place", 50, 2));

        // Display the player
        gameboardView.displayPlayer(player);

        // Verify that player information is displayed
        assertTrue(outputStreamCaptor.toString().contains("Alice --> P1"));
        assertTrue(outputStreamCaptor.toString().contains("|---Money: HKD 1500"));
        assertTrue(outputStreamCaptor.toString().contains("|---Position: 1"));
        assertTrue(outputStreamCaptor.toString().contains("    |---Park Place"));
    }

    @Test
    void testDisplayGameboard() {
        // This test assumes gameboardString is already initialized in GameboardView
        gameboardView.displayGameboard();

        // Verify that the gameboard string is displayed
        assertTrue(outputStreamCaptor.toString().contains("MONOPOLY")); // Check for a known part of the gameboard
    }

    @Test
    void testUpdateGameboard() {
        // Setup a gameboard and players
        Gameboard gameboard = new Gameboard();
        Player player1 = new Player(1, "Alice", 1500, 1);
        Player player2 = new Player(2, "Bob", 1300, 2);
        gameboard.addPlayer(player1);
        gameboard.addPlayer(player2);

        // Update the gameboard view
        gameboardView.updateGameboard(gameboard);

        // Call displayGameboard to output the current state
        gameboardView.displayGameboard();

        // Verify that player information is reflected in the gameboard
        assertTrue(outputStreamCaptor.toString().contains("P1"));
        assertTrue(outputStreamCaptor.toString().contains("P2"));
    }
}