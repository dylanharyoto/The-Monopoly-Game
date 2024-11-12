package controller;

import model.*;
import view.GameboardView;
import view.InputView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameboardController {
    private Gameboard gameboard;
    private GameboardView gameboardView;
    public GameboardController(Gameboard gameboard, GameboardView gameboardView) {
        this.gameboard = gameboard;
        this.gameboardView = gameboardView;
    }
    private static void handleDelete(String input, ArrayList<String> names) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            InputView.displayMessage("Please specify a player name to delete.\n");
            return;
        }
        String name = parts[1].trim();
        if (!names.remove(name)) {
            InputView.displayMessage("Player not found.\n");
        }
    }

    private static void handleAdd(ArrayList<String> names, int playerNumber) {
        String name = InputView.inputPrompt("",new String[]{});
        if (name.length() > 10 || name.isEmpty()) {
            InputView.displayMessage("One of the name length isn't permitted.");
        } else if (names.size() == playerNumber) {
            InputView.displayMessage("Slots are taken, player '" + name.trim() + "' is ignored.");
        } else {
            names.add(name.trim());
        }
    }

    public boolean newGame() {
        boolean proceed = false;
        ArrayList<String> names = new ArrayList<String>(0);
        String choice = InputView.inputPrompt("Please enter the number of players (minimum 2, maximum 6)", new String[]{"2", "3", "4", "5", "6"});
        int playerNumber = Integer.parseInt(choice);
        if (playerNumber >= 2 && playerNumber <= 6) {
            names = new ArrayList<String>(playerNumber);
            while (true) {
                InputView.displayMessage("Current players:");
                if (names.isEmpty()) {
                    InputView.displayMessage("None");
                } else {
                    for (int i = 0; i < names.size(); i++) {
                        InputView.displayMessage((i + 1) + ". " + names.get(i));
                    }
                }
                String option = InputView.inputPrompt("Would you like to \n1. Add more players\n2. Start the game", new String[]{"1", "2"});
                if (option.equals("1")) {
                    handleAdd(names, playerNumber);
                } else if (option.equals("2")) {
                    if (names.size() != playerNumber) {
                        InputView.displayMessage("Oops...There's still an empty slot.\n");
                    } else {
                        break;
                    }
                }
            }
            while(true){
                choice = InputView.inputPrompt("Would you like to\n1. Start with default map\n2. Start by loading a map", new String[]{"1", "2"});
                String curdir = System.getProperty("user.dir");
                if(choice.equals("1")) {
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/defaultMap", gameboard))
                        continue;
                } else if(choice.equals("2")) {
                    String filename = InputView.promptFilename("Please input the json filename");
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/" + filename, gameboard)){
                        InputView.displayMessage("File not exist!");
                        continue;
                    }
                }
                break;
            }
        } else {
            InputView.displayMessage("This is not a valid number of players!");
        }
        for (int i = 0; i < names.size(); i++) {
            gameboard.addPlayer(new Player(i+1, names.get(i), 1500, 1));
        }
        startGame();
        return true;
    }
    public void startGame() {
        for(Square s: gameboard.getAllSquares()) gameboardView.replaceBlockBySquare(s);
        InputView.displayMessage("Welcome to MonoPolyU, the game is starting...");
        Player currentPlayer;
        while (gameboard.checkGameStatus()) {
            currentPlayer = this.gameboard.getPlayerById(this.gameboard.getCurrentPlayerId());
            int currentPlayerInitialPosition = currentPlayer.getPosition();
            boolean proceed = false;
            while(!proceed) {
                String option = InputView.inputPrompt(currentPlayer.getName() +
                        """
                        's turn. Would you like to:
                        1. Roll the dice (required to proceed)
                        2. Check player(s) status
                        3. Print the current gameboard status
                        4. Check the next player
                        5. Save the current game""", new String[]{"1", "2", "3", "4", "5"});
                switch (option) {
                    case "1":
                        currentPlayer.rollDice();
                        proceed = true;
                        break;
                    case "2":
                        String playerStatusOption = InputView.inputPrompt(
                    """
                        Would you like to:
                        1. Go back to previous options
                        2. Check all players
                        3. Check a single player
                        """,
                        new String[]{"1", "2", "3"});
                        switch (playerStatusOption) {
                            case "1" :
                                break;
                            case "2" :
                                gameboardView.displayPlayers(gameboard.getAllPlayers());
                                break;
                            case "3" :
                                int playerId = InputView.promptGetPlayer(gameboard.getAllPlayers(), gameboard.getTotalPlayers());
                                gameboardView.displayPlayer(gameboard.getPlayerById(playerId));
                                break;
                        }
                        break;
                    case "3":
                        this.gameboardView.updateGameboard(gameboard);
                        this.gameboardView.displayGameboard();
                        break;
                    case "4":
                        int nextPlayerId = gameboard.getNextPlayerId();
                        gameboardView.displayPlayer(gameboard.getPlayerById(nextPlayerId));
                        break;
                    case "5":
                        String curdir = System.getProperty("user.dir");
                        String filename =  curdir + "/assets/games/" + this.gameboard.generateGameID() + ".json";
                        GameboardManager.saveGame(gameboard, filename);
                        break;
                    default:
                        InputView.displayMessage("You must roll the dice (type \"1\") before proceeding.");
                        break;
                }
            }
            int currentPlayerCurrentPosition = currentPlayer.getPosition();
            int currentGoPosition = gameboard.getGoPosition();
            if (currentPlayerInitialPosition == currentPlayerCurrentPosition) {
                String jailOption = InputView.inputPrompt("""
                    You are currently in jail. Would you like to:
                    1. Pay HKD$150 to get out
                    2. Stay in jail
                    """, new String[]{"1", "2"});
                if (jailOption.equals("1")) {
                    currentPlayer.decreaseMoney(150);
                    currentPlayer.setInJailDuration(0);
                }
//
            } else {
                if (8 >= currentPlayerCurrentPosition && 13 <= currentPlayerInitialPosition && currentPlayerCurrentPosition != 1) {
                    gameboard.getSquareByPosition(currentGoPosition).takeEffect(currentPlayer);
                }
            }
            gameboard.getSquareByPosition(currentPlayerCurrentPosition).takeEffect(currentPlayer);
            if (currentPlayer.getMoney() < 0) {
                for (Property property : currentPlayer.getProperties()) {
                    property.setOwner(null);
                }
                InputView.displayMessage(currentPlayer.getName() + " is out of the game.");
                gameboard.removePlayer(currentPlayer);
            }
            gameboard.nextPlayer();
        }
        endGame();
    }
    public void endGame() {
        int[] winnersId = gameboard.getWinners();

        if (winnersId.length == 1) {
            InputView.displayMessage("Game Finished! The winner is " + gameboard.getPlayerById(winnersId[0]).getName());
        } else {
            StringBuilder winnerNames = new StringBuilder("Game Finished! The winners are ");
            for (int i = 0; i < winnersId.length; i++) {
                winnerNames.append(gameboard.getPlayerById(winnersId[i]).getName());
                if (i != winnersId.length - 1) {
                    winnerNames.append(", ");
                }
            }
            InputView.displayMessage(winnerNames.toString());
        }
    }
}
