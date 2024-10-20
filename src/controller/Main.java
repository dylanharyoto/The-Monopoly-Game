package controller;

import model.Gameboard;
import model.GameboardManager;
import view.GameboardView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Gameboard gameboard = new Gameboard();
        GameboardView gameboardView = new GameboardView();
        GameboardController gameboardController = new GameboardController(gameboard, gameboardView);
        Scanner scanner = new Scanner(System.in);
        String curdir = System.getProperty("user.dir");
        while(true) {
            int choice = Integer.parseInt(gameboardView.inputPrompt("Welcome to MonoPolyU!\nWould you like to\n1. Start new game\n2. Load existing game", new String[]{"1", "2"}));
            if(choice == 1) {
                if(gameboardController.newGame()) break;
            }
            else if(choice == 2) {
                System.out.print("Please input the json filename here\n> ");
                String filename = scanner.next();
                filename = filename.endsWith(".json") ? filename : filename + ".json";
                if(GameboardManager.loadGame(curdir + "/assets/games/" + filename, gameboard)) {
                    gameboardController.startGame();
                    break;
                }
            }
        }
        System.out.println("Thanks for playing!");

    }
}
