import controller.GameboardController;
import model.Gameboard;
import model.GameboardManager;
import view.GameboardView;
import view.InputView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        Gameboard gameboard = new Gameboard();
        GameboardView gameboardView = new GameboardView();
        GameboardController gameboardController = new GameboardController(gameboard, gameboardView);
        String curdir = System.getProperty("user.dir");
        while(true) {
            int choice = Integer.parseInt(InputView.inputPrompt("Welcome to MonoPolyU!\nWould you like to\n1. Start new game\n2. Load existing game", new String[]{"1", "2"}));
            if(choice == 1) {
                if(gameboardController.newGame()) break;
            } else if(choice == 2) {
                String filename = InputView.promptFilename("Please input the json filename here");
                filename = filename.endsWith(".json") ? filename : filename + ".json";
                if(GameboardManager.loadGame(curdir + "/assets/games/" + filename, gameboard)) {
                    gameboardController.startGame();
                    break;
                }
            }
        }
        InputView.displayMessage("Thanks for playing!");

    }
}
