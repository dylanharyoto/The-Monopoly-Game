package test;
import main.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class IncomeTaxTest {
    private IncomeTax incomeTaxSquare;
    private Player player;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));

        // Initialize IncomeTax square and Player instances
        incomeTaxSquare = new IncomeTax(1, "TAX");
        player = new Player(1, "Alice", 500, 1); // Adjust constructor as necessary
    }

    @Test
    void testTakeEffect_withValidPlayer() {
        // Call takeEffect method
        incomeTaxSquare.takeEffect(player);

        // Verify the player's money after tax deduction
        assertEquals(450, player.getMoney()); // 10% of 500 is 50, so 500 - 50 = 450

        // Capture the output and verify it
        String output = outputStream.toString();
        assertTrue(output.contains("Alice needs to pay a tax of 50HKD (10% of the player's money)!\n"));
    }

    @Test
    void testTakeEffect_withPlayerNull() {
        // Verify that an exception is thrown when player is null
        incomeTaxSquare.takeEffect(null);
        String output = outputStream.toString();

        assertTrue(output.contains("Player cannot be null!"));
    }

    @Test
    void testTakeEffect_withNoMoney() {
        // Set player's money to 0
        player.decreaseMoney(500); // Assuming the player starts with 500
        assertEquals(0, player.getMoney()); // Now player's money is 0

        // Call takeEffect method
        incomeTaxSquare.takeEffect(player);

        // Verify that player's money remains 0
        assertEquals(0, player.getMoney());
    }

    @Test
    void testTypeDetailsJson() {
        String expectedJson = "\"details\": {}\n";
        assertEquals(expectedJson, incomeTaxSquare.detailsInJSON());
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}