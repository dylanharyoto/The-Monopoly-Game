import main.model.FreeParking;
import main.model.Player;
import main.view.InputView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class FreeParkingTestTwo {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private FreeParking freeParking;
    private Player player;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        freeParking = new FreeParking(5, "FP1");
        player = new Player(2, "Bob", 1000, 5);
    }

    @Test
    void testTakeEffect() {
        System.out.flush();
        freeParking.takeEffect(player);
        System.out.flush();
        assertEquals("Free Parking square, nothing happens.", outContent.toString().trim());
    }

    @Test
    void testtypeDetailsJson(){freeParking.typeDetailsJson();}

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
}