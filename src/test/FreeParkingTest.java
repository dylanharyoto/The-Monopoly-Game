package main.model;

import main.view.InputView;
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
        assertTrue(output.contains("Free Parking square, nothing happens."));
    }

    @Test
    void testTypeDetailsJson() {
        String expectedJson = "\"type\": \"F\",\n\"details\": {}\n";
        assertEquals(expectedJson, freeParking.typeDetailsJson());
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}