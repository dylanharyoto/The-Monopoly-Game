package controller;

import model.*;
import view.GameboardView;
import view.InputView;

import java.util.ArrayList;
import java.util.Scanner;

public class GameboardController {
    private Gameboard gameboard;
    private GameboardView gameboardView;
    public GameboardController(Gameboard gameboard, GameboardView gameboardView) {
        this.gameboard = gameboard;
        this.gameboardView = gameboardView;
    }
    public boolean newGame() {
        boolean proceed = false;
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> names = new ArrayList<String>(0);
        String choice = InputView.inputPrompt("Please enter the number of players (minimum 2, maximum 6)", new String[]{"2", "3", "4", "5", "6"});
        int playerNumber = Integer.parseInt(choice);
        if (playerNumber >= 2 && playerNumber <= 6) {
            names = new ArrayList<String>(playerNumber);
            InputView.displayMessage("""
            Here you can add or delete players by
            Enter 'add xx' to add player, or
            Enter 'delete xx' to delete player,
            where xx is the name of player.
            To change the number of players, type 'return'.""");
            while (!proceed) {
                InputView.displayMessage("When players are ready, type 'p' to proceed. Current players:");
                if (names.isEmpty())
                    InputView.displayMessage("None");
                for (int i = 0; i < names.size(); i++) {
                    InputView.displayMessage((i + 1) + ". " + names.get(i));
                }
                String nameInput = InputView.inputPrompt("",new String[]{});
                if (nameInput.startsWith("delete")) {
                    String name = InputView.inputPrompt("",new String[]{});
                    names.remove(name);
                } else if (nameInput.equals("return")) {
                    break;
                } else if (nameInput.equals("p")) {
                    if (names.size() != playerNumber) {
                        InputView.displayMessage("Oops...There's still empty slot");
                        continue;
                    }
                    break;
                } else if (nameInput.startsWith("add")){
                    String name = InputView.inputPrompt("",new String[]{});
                    if (name.length() > 10 || name.isEmpty())
                        InputView.displayMessage("One of the name length isn't permitted.");
                    else if (names.size() == playerNumber)
                        InputView.displayMessage("Slots are taken, player " + name.trim() + " is ignored.");
                    else
                        names.add(name.trim());

                }

            }
            while(!proceed){
                choice = InputView.inputPrompt("Would you like to\n1. start with default map\n2. start by loading map", new String[]{"1", "2"});
                String curdir = System.getProperty("user.dir");
                if(choice.equals("1")) {
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/map2", gameboard))
                        continue;
                } else{
                    String filename = InputView.promptFilename("Please input the json filename");
                    if(!GameboardManager.loadMap(curdir + "/assets/maps/" + filename, gameboard)){
                        InputView.displayMessage("File not exist!");
                        continue;
                    }

                }
                for(Square s: gameboard.getAllSquares()) gameboardView.replaceBlockBySquare(s);

                proceed = true;
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
        InputView.displayMessage("Welcome to MonoPolyU, the game is starting...");
        Player currentPlayer;

        while (gameboard.checkGameStatus()) {
            currentPlayer = this.gameboard.getPlayerById(this.gameboard.getCurrentPlayerId());
            int currentPlayerInitialPosition = currentPlayer.getPosition();
            boolean proceed = false;
            while(!proceed) {
                String[] options = {"0", "1", "2", "3"};
                String option = InputView.inputPrompt(currentPlayer.getName() +
                        """
                        's turn. Would you like to:
                        "0" Roll the dice (required to proceed)
                        "1" Check player(s) status
                        "2" Print the current gameboard status
                        "3" Check the next player
                        "4" Save the current game
                        """, options);
                switch (option) {
                    case "0":
                        currentPlayer.rollDice();
                        this.gameboard.getSquareByPosition(currentPlayer.getPosition()).takeEffect(currentPlayer);
                        proceed = true;
                        break;
                    case "1":
                        String[] playerStatusOptions = {"1", "2", "0"};
                        String playerStatusOption = InputView.inputPrompt(
                                """
                                    Would you like to:
                                    "1" Check all players
                                    "2" Check a single player
                                    "0" Go back to previous options
                                    """,
                                playerStatusOptions);
                        if (playerStatusOption.equals("1")) {
                            gameboardView.displayPlayers(gameboard.getAllPlayers());
                        } else if (playerStatusOption.equals("0")) {
                            break;
                        } else {
                            int playerId = InputView.promptGetPlayer(gameboard.getTotalPlayers());
                            gameboardView.displayPlayer(gameboard.getPlayerById(playerId));
                        }
                        // to be implemented (check player by name)
                        break;
                    case "2":
                        // to be implemented
                        this.gameboardView.updateGameboard(gameboard);
                        this.gameboardView.displayGameboard();
                        break;
                    case "3":
                        // to be implemented
                        int nextPlayerId = gameboard.getNextPlayerId();
                        gameboardView.displayPlayer(gameboard.getPlayerById(nextPlayerId));
                    case "4":
                        // to be implemented
                        String curdir = System.getProperty("user.dir");
                        String filename =  curdir + this.gameboard.getGameID() + ".json";
                        GameboardManager.saveGame(gameboard, filename);
                        break;
                    default:
                        InputView.displayMessage("You must roll the dice (\"0\") before proceeding.");
                        break;
                }
            }
            int currentPlayerCurrentPosition = currentPlayer.getPosition();
            int currentGoPosition = gameboard.getGoPosition();
            if (currentPlayerInitialPosition == currentPlayerCurrentPosition) {
                String[] jailOptions = {"1", "2"};
                String jailOption = InputView.inputPrompt("""
                    You are currently in jail. Would you like to:
                    1. Pay HKD$150 to get out
                    2. Stay in jail
                    """, jailOptions);
                if (jailOption.equals("1")) {
                    currentPlayer.decreaseMoney(150);
                    currentPlayer.setInJailDuration(0);
                } else {
                    currentPlayer.setInJailDuration(currentPlayer.getInJailDuration() - 1);
                }
            } else {
                if (currentGoPosition <= currentPlayerCurrentPosition && currentGoPosition > currentPlayerInitialPosition) {
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
