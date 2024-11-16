package main.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTestTwo {
    private Player player;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeEach
    public void setUp() {
        player = new Player(1, "John", 1000, 1);
        System.setOut(new PrintStream(outputStream));

    }

    @Test
    public void testGetId() {
        assertEquals(1, player.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("John", player.getName());
    }

    @Test
    public void testGetMoney() {
        assertEquals(1000, player.getMoney());
    }

    @Test
    public void testJailPunishment(){
        Square goJail = new GoJail(1, "1");
        while (player.getMoney() == 1000){
            goJail.takeEffect(player);
            while (player.getInJailDuration() != 0){
                player.rollDice();
            }
        }
        assertEquals(850, player.getMoney());
    }

    @Test
    public void testIncreaseMoney() {
        player.increaseMoney(200);
        assertEquals(1200, player.getMoney());
    }

    @Test
    public void testDecreaseMoney() {
        player.decreaseMoney(200);
        assertEquals(800, player.getMoney());
    }

    @Test
    public void testDecreaseMoney_throwsExceptionForNegativeAmount() {
        player.decreaseMoney(-100);
        String output = outputStream.toString();
        assertTrue(output.contains("Amount to decrease cannot be negative!\n"));
    }

    @Test
    public void testIncreaseMoney_throwsExceptionForNegativeAmount() {
        player.increaseMoney(-100);
        String output = outputStream.toString();
        assertTrue(output.contains("Amount to increase cannot be negative!\n"));
    }

    @Test
    public void testSetInJailDuration() {
        player.setInJailDuration(3);
        assertEquals(3, player.getInJailDuration());
    }

    @Test
    public void testSetInJailDuration_throwsExceptionForNegativeDuration() {
        player.setInJailDuration(-1);
        String output = outputStream.toString();
        assertTrue(output.contains("Jail duration cannot be negative!\n"));
    }

    @Test
    public void testAddProperty() {
        Property property = new Property(3,"3","abc", 300, 30); // Assuming a default constructor exists
        player.addProperty(property);
        assertEquals(1, player.getProperties().size());
    }

    @Test
    public void testAddProperty_throwsExceptionForNullProperty() {
        player.addProperty(null);
        String output = outputStream.toString();
        assertTrue(output.contains("Property cannot be null!\n"));
    }

    @Test
    public void testSetPosition() {
        player.setPosition(5);
        assertEquals(5, player.getPosition());
    }

    @Test
    public void testSetPosition_throwsExceptionForInvalidPosition() {
        player.setPosition(21);
        String output = outputStream.toString();
        assertTrue(output.contains("Position must be between 1 and 20!\n"));
    }

    @Test
    public void testRollDice() {
        // This test is a bit tricky due to randomness. You may want to mock Random for a more deterministic test.
        player.rollDice(); // Just call it, as we can't predict the outcome.
        // Verify some outcome based on expected behavior
        // This might involve checking position, money, etc., after rolling.
    }

    @Test
    public void testGetStatus(){
        assertTrue(player.getStatus());
    }

    @Test
    public void testSetStatus(){
        player.setStatus(false);
        assertFalse(player.getStatus());
    }

    @AfterEach
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}