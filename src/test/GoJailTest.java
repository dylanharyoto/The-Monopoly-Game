package test;


import main.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GoJailTest {
    private GoJail goJailSquare;
    private Player player;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));

        // Initialize GoJail square and Player instances
        goJailSquare = new GoJail(1, "JAIL");
        player = new Player(1, "Alice", 500, 1); // Adjust constructor as necessary
    }

    @Test
    void testTakeEffect() {
        // Call takeEffect method
        goJailSquare.takeEffect(player);

        // Verify the player's position and jail duration
        assertEquals(6, player.getPosition()); // Assuming jail position is 6
        assertEquals(3, player.getInJailDuration()); // Assuming setInJailDuration sets the duration

        // Capture the output and verify it
        String output = outputStream.toString();
        assertTrue(output.contains("Alice is sent to jail!\n"));
    }

    @Test
    void testTypeDetailsJson() {
        String expectedJson = "\"details\": {}\n";
        assertEquals(expectedJson, goJailSquare.detailsInJSON());
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}