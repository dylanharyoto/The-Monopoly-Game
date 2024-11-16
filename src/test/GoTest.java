import main.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GoTest {
    private Go goSquare;
    private Player player;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));

        // Initialize Go square and Player instances
        goSquare = new Go(1, "GO");
        player = new Player(1, "Alice", 500, 1); // Assuming Player constructor takes (id, name, money, position)
    }

    @Test
    void testTakeEffect_withValidPlayer() {
        // Call takeEffect method
        goSquare.takeEffect(player);

        // Verify the player's money has increased
        assertEquals(2000, player.getMoney()); // Assuming initial money was 500 + 1500 bonus

        // Capture the output and verify it
        String output = outputStream.toString();
        assertTrue(output.contains("Alice gets 1500HKD from GO!\n"));
    }

    @Test
    void testTakeEffect_withNullPlayer() {
        // Verify that an exception is thrown when player is null
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            goSquare.takeEffect(null);
        });

        assertEquals("Player cannot be null.", exception.getMessage());
    }

    @Test
    void testTypeDetailsJson() {
        String expectedJson = "\"details\": {}\n";
        assertEquals(expectedJson, goSquare.detailsInJSON());
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}