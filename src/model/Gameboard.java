package model;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Gameboard {
    private String gameID = ""; // null means this is a new game without loading process
    private String mapID = "";  // null means this is a new map without loading process
    private ArrayList<Player> players;
    private ArrayList<Square> squares;
    private int round;
    private int currentPlayerId; // when we load from / save to a gamefile, we should load from / save to this attribute to record the current player
    private int goPosition;
    public Gameboard() {
        this.players = new ArrayList<Player>(6);
        this.squares = new ArrayList<Square>(20);
        this.round = 1;
        this.currentPlayerId = 1;
    }
    // Game ID
    public String generateGameID() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.gameID = now.format(formatter);
        return gameID;
    }
    public String getGameID() {
        return gameID;
    }
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
    // Map ID
    public void setMapID(String mapID) {
        this.mapID = mapID;
    }
    public String getMapID() {
        return mapID;
    }
    // Player
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public Player removePlayer(Player player) {
        player.setStatus(false);
        return player;
    }
    public ArrayList<Player> getAllPlayers() {
        return this.players;
    }
    public int getTotalPlayers() {
        return this.players.size();
    }
    public Player getPlayerById(int playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }
    public int getNextPlayerId() {
        if(this.players.isEmpty()) {
            return -1;
        }
        int totalPlayers = this.players.size();
        int nextPlayerId;
        do {
            nextPlayerId = (this.currentPlayerId + 1) % (totalPlayers + 1);
            if (nextPlayerId == 0) {
                nextPlayerId = 1;
            }
        } while (this.getPlayerById(nextPlayerId).getMoney() < 0);
        return nextPlayerId;
    }
    public void nextPlayer() {
        if (this.currentPlayerId > this.getNextPlayerId()) {
            this.round = this.round + 1;
        }
        this.currentPlayerId = this.getNextPlayerId();
    }
    // Square
    public void addSquare(Square square) {
        this.squares.add(square);
    }
    public ArrayList<Square> getAllSquares() {
        return this.squares;
    }
    public Square getSquareByPosition(int position) {
        if (position <= squares.size() && position > 0)
            return this.squares.get(position-1);
        else
            return null;
    }
    // Additional
    public boolean checkGameStatus() {
        return this.round < 100 && (players.size() > 1 && players.size() < 7);
    }
    public int getCurrentPlayerID() {
        return this.currentPlayerId;
    }
    public void setGoPosition(int position) {
        this.goPosition = position;
    }
    public int getGoPosition() {
        return this.goPosition;
    }
    public int[] getWinners() {
        return this.getAllPlayers().stream()
            .filter(Player::getStatus)
            .mapToInt(Player::getId)
            .toArray();
    }
}
