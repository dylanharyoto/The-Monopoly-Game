package controller;

import model.Gameboard;
import model.GameboardManager;
import model.Player;
import model.Property;
import view.GameboardView;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameboardController {
    private Gameboard gameboard;
    private GameboardView gameboardView;
    public GameboardController(Gameboard gameboard, GameboardView gameboardView) {
        this.gameboard = gameboard;
        this.gameboardView = gameboardView;
    }
    private boolean newGame() {
        boolean proceed = false;
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> names = new ArrayList<String>(0);
        String choice = gameboardView.inputPrompt("The first step of starting a new game is to choose 2~6 players, please enter number of players below",new String[]{"2", "3", "4", "5", "6"});
        int playerNumber = Integer.parseInt(choice);
        if (playerNumber >= 2 && playerNumber <= 6) {
            names = new ArrayList<String>(playerNumber);
            System.out.println("""
                    Now you can type in the names of the players.
                    You can type multiple names at a time seperated by ';' symbol (max name length is 10 characters)
                    Or type '!d#' where # is the id of the name you want to delete. (for example '!d2')
                    If you like to resize the number of player, type '!r'.""");

            while (!proceed) {
                System.out.println("When players are ready, type '!p' to proceed. Current players:");
                if (names.isEmpty())
                    System.out.println("None");
                for (int i = 0; i < names.size(); i++) {
                    System.out.println((i + 1) + ". " + names.get(i));
                }
                System.out.print("> ");
                String nameInput = scanner.next();
                if (nameInput.startsWith("!d")) {
                    int deleteChoice = -1;
                    try {
                        deleteChoice = Integer.parseInt(nameInput.substring(2, 3));
                        if (deleteChoice < 1 || deleteChoice > names.size())
                            throw new NumberFormatException();
                    } catch (InputMismatchException | NumberFormatException e) {
                        System.out.println("Your input is invalid!");
                        continue;
                    }
                    names.remove(deleteChoice - 1);
                } else if (nameInput.equals("!r")) {
                    break;
                } else if (nameInput.equals("!p")) {
                    if (names.size() != playerNumber) {
                        System.out.println("Oops...There's still empty slot");
                        continue;
                    }
                    break;
                }else {
                    for (String substringName : nameInput.split(";")) {
                        if (substringName.length() > 10 || substringName.isEmpty())
                            System.out.println("One of the name length isn't permitted.");
                        else if (names.size() == playerNumber)
                            System.out.println("Slots are taken, player " + substringName.trim() + " is ignored.");
                        else
                            names.add(substringName.trim());
                    }
                }

            }
            while(!proceed){
                choice = gameboardView.inputPrompt("Would you like to\n1. start with default map\n2. start by loading map", new String[]{"1", "2"});

                if(choice.equals("1")) {
                    String curdir = System.getProperty("user.dir");
                    if(!GameboardManager.loadGame(curdir+"/src/map1", 0))continue;
                    proceed = true;
                }
                else{
                    String filename = gameboardView.promptFilename("Please input the json filename");
                    if(GameboardManager.loadGame(filename, 0)){
                        System.out.println("File not exist!");
                        continue;
                    }
                    proceed = true;
                }

            }
            playerNumber = names.size();
        } else {
            System.out.println("This is not a valid number of players!");
        }

        for (int i = 0; i < names.size(); i++) {
            gameboard.addPlayer(new Player(i, names.get(i), 1500, 1));
        }
        startGame();
        return true;
    }
    public void startGame() {
        this.gameboardView.displayMessage("Welcome to MonoPolyU, the game is starting...");
        Player currentPlayer;
        while (gameboard.checkGameStatus()) {
            currentPlayer = this.gameboard.getPlayerById(this.gameboard.getCurrentPlayerId());
            int currentPlayerInitialPosition = currentPlayer.getPosition();
            boolean proceed = false;
            while(!proceed) {
                String[] options = {"0", "1", "2", "3"};
                String option = gameboardView.inputPrompt(currentPlayer.getName() +
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
                        proceed = true;
                        break;
                    case "1":
                        String[] playerStatusOptions = {"1", "2"};
                        String playerStatusOption = gameboardView.inputPrompt(
                                """
                                    Would you like to:
                                    "1" Check all players
                                    "2" Check a single player
                                    "0" Go back to previous options
                                    """,
                                playerStatusOptions);
                        if(playerStatusOption.equals("1")) {
                            gameboardView.displayPlayers(gameboard.getAllPlayers());
                        } else {
                            int playerId = gameboardView.promptGetPlayer(gameboard.getTotalPlayers());
                            gameboardView.displayPlayer(gameboard.getPlayerById(playerId));
                        }
                        // to be implemented (check player by name)
                        break;
                    case "2":
                        // to be implemented
                        this.gameboardView.displayGameboard();
                        break;
                    case "3":
                        // to be implemented
                        int nextPlayerId = gameboard.getNextPlayerId();
                        gameboardView.displayPlayer(gameboard.getPlayerById(nextPlayerId));
                    case "4":
                        // to be implemented
                        String filename = this.gameboardView.getFilenameInput();
                        GameboardManager.saveGame(gameboard, filename);
                        break;
                    default:
                        gameboardView.displayMessage("You must roll the dice (\"0\") before proceeding.");
                        break;
                }
            }
            int currentPlayerCurrentPosition = currentPlayer.getPosition();
            int currentGoPosition = gameboard.getGoPosition();
            if (currentPlayerInitialPosition == currentPlayerCurrentPosition) {
                String[] jailOptions = {"1", "2"};
                String jailOption = gameboardView.inputPrompt("""
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
            } else if (currentPlayerInitialPosition < currentPlayerCurrentPosition) {
                if (currentGoPosition <= currentPlayerCurrentPosition && currentGoPosition > currentPlayerInitialPosition) {
                    gameboard.getSquareByPosition(currentGoPosition).takeEffect(currentPlayer);
                }
            } else {
                if (currentGoPosition <= currentPlayerCurrentPosition || currentGoPosition > currentPlayerInitialPosition) {
                    gameboard.getSquareByPosition(currentGoPosition).takeEffect(currentPlayer);
                }
            }
            gameboard.getSquareByPosition(currentPlayerCurrentPosition).takeEffect(currentPlayer);
            if (currentPlayer.getMoney() < 0) {
                for (Property property : currentPlayer.getProperties()) {
                    property.setOwner(null);
                }
                gameboardView.displayMessage(currentPlayer.getName() + " is out of the game.");
                gameboard.removePlayer(currentPlayer);
            }
            gameboard.nextPlayer();
        }
        endGame();
    }
    public void endGame() {
        int[] winnersId = gameboard.getWinners();

        if (winnersId.length == 1) {
            gameboardView.displayMessage("Game Finished! The winner is " + gameboard.getPlayerById(winnersId[0]).getName());
        } else {
            StringBuilder winnerNames = new StringBuilder("Game Finished! The winners are ");
            for (int i = 0; i < winnersId.length; i++) {
                winnerNames.append(gameboard.getPlayerById(winnersId[i]).getName());
                if (i != winnersId.length - 1) {
                    winnerNames.append(", ");
                }
            }
            gameboardView.displayMessage(winnerNames.toString());
        }
    }
}
