package test;
import main.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class FreeParkingTest {
    private FreeParking freeParking;
    private Player player;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));

        // Initialize FreeParking and Player instances
        freeParking = new FreeParking(1, "FP");
        player = new Player(1, "Alice", 500, 1); // Assuming Player constructor takes (id, name, money, position)
    }

    @Test
    void testTakeEffect() {
        // Call takeEffect method
        freeParking.takeEffect(player);

        // Capture the output and verify it
        String output = outputStream.toString();
        assertTrue(output.contains("Free parking, nothing happens!\n"));
    }

    @Test
    void testTypeDetailsJson() {
        String expectedJson ="\"details\": {}\n";
        assertEquals(expectedJson, freeParking.detailsInJSON());
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}