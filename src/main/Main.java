package main;

import main.controller.GameboardController;
import main.model.Gameboard;
import main.model.GameboardManager;
import main.view.GameboardView;
import main.view.InputOutputView;


public class Main {
    public static void main(String[] args) {

        while(true) {
            Gameboard gameboard = new Gameboard();
            GameboardView gameboardView = new GameboardView();
            GameboardController gameboardController = new GameboardController(gameboard, gameboardView);
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
                    return;
            }
        }

    }
}
