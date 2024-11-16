import main.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class GameboardTest {
    private Gameboard gameboard;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        gameboard = new Gameboard();
        player1 = new Player(1, "Alice", 500, 1); // Adjust as necessary for your Player constructor
        player2 = new Player(2, "Bob", 600, 1); // Adjust as necessary for your Player constructor
    }

    @Test
    void testAddPlayer() {
        gameboard.addPlayer(player1);
        gameboard.addPlayer(player2);

        assertEquals(2, gameboard.getAllPlayers().size());
        assertTrue(gameboard.getAllPlayers().contains(player1));
        assertTrue(gameboard.getAllPlayers().contains(player2));
    }

    @Test
    void testRemovePlayer() {
        assertNull(gameboard.getNextPlayer());
        gameboard.addPlayer(player1);
        gameboard.removePlayer(player1);

        assertEquals(0, gameboard.getAllPlayers().size());
        assertFalse(player1.getStatus()); // Assuming getStatus() returns the player's status
    }

    @Test
    void testGetPlayerById() {
        gameboard.addPlayer(player1);
        gameboard.addPlayer(player2);

        assertEquals(player1, gameboard.getPlayerByID(1));
        assertEquals(player2, gameboard.getPlayerByID(2));
        assertNull(gameboard.getPlayerByID(3)); // Non-existent player ID
    }

    @Test
    void testNextPlayer() {
        gameboard.addPlayer(player1);
        gameboard.addPlayer(player2);

        gameboard.nextPlayer();
        assertEquals(2, gameboard.getCurrentPlayerID()); // Should be Bob's turn

        // Progress to next player
        gameboard.nextPlayer();
        assertEquals(1, gameboard.getCurrentPlayerID()); // Should be Alice's turn

        // Progress again
        gameboard.nextPlayer();
        assertEquals(2, gameboard.getCurrentPlayerID()); // Back to Bob
    }

    @Test
    void testCheckGameStatus() {
        assertFalse(gameboard.checkGameStatus()); // New game should be valid

        gameboard.addPlayer(player1);
        gameboard.addPlayer(player2);
        gameboard.setGoPosition(1); // Example position
        assertEquals(1, gameboard.getGoPosition());

        assertTrue(gameboard.checkGameStatus()); // Should still be valid

        // Simulate reaching maximum rounds
        for (int i = 0; i < 198; i++) {
            gameboard.nextPlayer();
        }

        assertFalse(gameboard.checkGameStatus()); // Should be false after 100 rounds
    }

    @Test
    void testGetWinners() {
        gameboard.addPlayer(player1);
        gameboard.addPlayer(player2);
        player1.setStatus(true); // Player 1 is active
        player2.setStatus(false); // Player 2 is inactive

        int[] winners = gameboard.getWinners();
        assertArrayEquals(new int[]{1}, winners); // Only player 1 should be a winner
    }

    @Test
    void testAddSquare() {
        Square square = new FreeParking(1, "FP"); // Assuming FreeParking is a valid Square
        gameboard.addSquare(square);

        assertEquals(1, gameboard.getAllSquares().size());
        assertEquals(square, gameboard.getSquareByPosition(1));
    }

    @Test
    void testGetSquareByPosition() {
        Square square1 = new FreeParking(1, "FP");
        Square square2 = new Go(2, "GO");
        gameboard.addSquare(square1);
        gameboard.addSquare(square2);

        assertEquals(square1, gameboard.getSquareByPosition(1));
        assertEquals(square2, gameboard.getSquareByPosition(2));
        assertNull(gameboard.getSquareByPosition(3)); // Non-existent square
    }

    @Test
    void testTimeStamp(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStamp = now.format(formatter);
        assertEquals(timeStamp, gameboard.generateGameID());
    }

    @Test
    void testIDs() {
        gameboard.setGameID("testString");
        assertEquals("testString", gameboard.getGameID());
        gameboard.setMapID("testString");
        assertEquals("testString", gameboard.getMapID());
    }

    @AfterEach
    void tearDown() {
        gameboard = null;
        player1 = null;
        player2 = null;
    }
}