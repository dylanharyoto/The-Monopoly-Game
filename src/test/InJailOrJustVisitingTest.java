package main.model;

import main.view.InputView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class InJailOrJustVisitingTest {
    private InJailOrJustVisiting jailSquare;
    private Player player;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));

        // Initialize InJailOrJustVisiting square and Player instances
        jailSquare = new InJailOrJustVisiting(1, "VISIT");
        player = new Player(1, "Alice", 500, 1); // Adjust constructor as necessary
    }

    @Test
    void testTakeEffect_withPlayerInJail() {
        // Set the player's jail duration to a positive number
        player.setInJailDuration(2); // Assuming this sets the duration

        // Call takeEffect method
        jailSquare.takeEffect(player);

        // Capture the output and verify it
        String output = outputStream.toString();
        assertTrue(output.contains("Alice needs to stay in the jail for 2 more rounds."));
    }

    @Test
    void testTakeEffect_withPlayerJustVisiting() {
        // Set the player's jail duration to 0
        player.setInJailDuration(0); // Assuming this sets the duration

        // Call takeEffect method
        jailSquare.takeEffect(player);

        // Capture the output and verify it
        String output = outputStream.toString();
        assertTrue(output.contains("Just Visiting (jail) square, nothing happens."));
    }

    @Test
    void testTypeDetailsJson() {
        String expectedJson = "\"type\": \"V\",\n\"details\": {}\n";
        assertEquals(expectedJson, jailSquare.typeDetailsJson());
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}