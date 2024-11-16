import main.model.*;
import main.view.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PropertyTestTwo {
    private Property property;
    private Player owner;
    private Player player;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));

        property = new Property(1, "P1", "Park Place", 300, 50);
        owner = new Player(1, "Alice", 500, 1);
        player = new Player(2, "Bob", 200, 1);
    }

    @Test
    public void testGetName() {
        assertEquals("Park Place", property.getName());
    }

    @Test
    public void testGetPrice() {
        assertEquals(300, property.getPrice());
    }

    @Test
    public void testGetRent() {
        assertEquals(50, property.getRent());
    }

    @Test
    public void testSetOwner() {
        property.setOwner(owner);
        assertEquals(owner, property.getOwner());
    }

    @Test
    public void testSetName() {
        property.setName("New Park Place");
        assertEquals("New Park Place", property.getName());
    }

    @Test
    public void testSetPrice() {
        property.setPrice(400);
        assertEquals(400, property.getPrice());
    }

    @Test
    public void testSetRent() {
        property.setRent(60);
        assertEquals(60, property.getRent());
    }

    @Test
    public void testTakeEffect_whenPropertyIsOwned() {
        property.setOwner(owner);
        player.decreaseMoney(200); // Set Bob's money to a value less than rent

        property.takeEffect(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Bob do not have enough money to pay the rent for Park Place!"));
    }

    @Test
    public void testTakeEffect_whenPropertyIsNotOwnedAndPlayerCanAfford() {
        player.increaseMoney(300); // Set Bob's money to 500

        // Simulate user choosing to buy
        InputOutputView.setInput("1"); // Set a method to provide input during testing

        property.takeEffect(player);

        assertEquals(200, player.getMoney()); // Bob should have enough money after buying
        assertEquals(player, property.getOwner()); // Bob should own the property
        assertTrue(player.getProperties().contains(property)); // Property should be in Bob's properties
        String output = outputStream.toString();
        assertTrue(output.contains("Thanks for buying Park Place for 300, Bob!"));
    }

    @Test
    public void testTakeEffect_whenPropertyIsNotOwnedAndPlayerCannotAfford() {
        player.decreaseMoney(200); // Bob has 0 money now

        // Simulate user choosing to buy
        InputOutputView.setInput("1"); // Set a method to provide input during testing

        property.takeEffect(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Bob does not have enough money to buy Park Place!"));
    }

    @Test
    public void testTypeDetailsJson() {
        String expectedJson = "\"type\": \"P\",\n\"details\": {\n\"name\": \"Park Place\",\n\"price\": 300,\n\"rent\": 50\n}";
        assertEquals(expectedJson, property.detailsInJSON());
    }

    // Clean up after tests
    @AfterEach
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}