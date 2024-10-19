package model;
import java.io.*;
import java.util.*;
import controller.Main;
public class Gameboard {
    private String id;
    private static int count = 0;
    private ArrayList<Player> players;
    private ArrayList<Square> squares;
    private ArrayList<Square> allSquares;
    private int round;
    private int currentPlayerId;
    private final int goPosition;
/*
    public Gameboard(int id) {
        this.id = id;
        this.players = new ArrayList<Player>(6);
        this.squares = new ArrayList<Square>(20);
        this.round = 1;
        this.currentPlayerId = -1;
        this.goPosition = -1;
    }
*/
public Gameboard() {
        this.players = new ArrayList<Player>(6);
        this.squares = new ArrayList<Square>(20);
        this.round = 1;
        this.currentPlayerId = -1;
        this.goPosition = -1;
    }
    public static int generateId() {
        count += 1;
        return count;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public Player removePlayer(Player player) {
        player.setStatus(false);
        return player;
    }
    public void addSquare(Square square) {
        this.squares.add(square);
    }
    public ArrayList<Player> getAllPlayers() {
        return this.players;
    }
    public ArrayList<Square> getAllSquares() {
        return this.squares;
    }
    public int getTotalPlayers() {
        return this.players.size();
    }
    public int getTotalSquares() {
        return this.squares.size();
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
        int nextPlayerId = (this.currentPlayerId + 1) % (totalPlayers + 1);
        do {
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
    public Square getSquareByPosition(int position) {
        if (position <= squares.size() && position >= 0)
            return this.squares.get(position);
        else
            return null;
    }
    public boolean checkGameStatus() {
        return this.round < 100 && (players.size() > 1 && players.size() < 7);
    }
    public int getRound() {
        return this.round;
    }
    public int getCurrentPlayerId() {
        return this.currentPlayerId;
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
