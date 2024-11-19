package main;

import main.controller.GameboardController;
import main.model.Gameboard;
import main.model.GameboardManager;
import main.model.Player;
import main.view.GameboardView;
import main.view.InputOutputView;

import java.io.PrintStream;


public class Main {
    public static void main(String[] args) {


        Gameboard gameboard = new Gameboard();
        GameboardView gameboardView = new GameboardView();
        GameboardController gameboardController = new GameboardController(gameboard, gameboardView);
        label:
        while(true) {
            String choice = InputOutputView.promptInput("""
            Welcome to MonoPolyU!
            Would you like to
            1. Start a new game
            2. Load an existing game
            3. Design a map
            4. Log Out""", new String[]{"1", "2", "3", "4"});
            switch (choice) {
                case "1":
                    gameboardController.newGame();
                    break;
                case "2":
                    if (GameboardManager.loadGame(gameboard)) {
                        gameboardController.startGame();
                    }
                    break;
                case "3":
                    GameboardController.designMap();
                    break;
                case "4":
                    InputOutputView.displayMessage("Thanks for coming to MonoPolyU!");
                    break label;
            }
        }


        //InputOutputView.setInput("1\n1\n0\n1\nNew Name\n3\n1\n4"); // Change name of property at index 0


    }
}
