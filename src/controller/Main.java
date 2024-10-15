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
        while(true) {
            int choice = Integer.parseInt(gameboardView.inputPrompt("Would you like to start a new game or load from an existing game?\n1. new game\n2.load game", new String[]{"1", "2"}));
            if(choice == 1) {
                if(gameboardController.newGame()) break;
            }else if(choice == 2) {
                while(true){
                    System.out.println("Please input the json filename here>");
                    String filename = scanner.next();
                    filename = filename.endsWith(".json") ? filename : filename + ".json";
                    if(GameboardManager.loadGame(filename, gameboard)) {
                        gameboardController.startGame();
                        break;
                    }

                }
            }
        }
        System.out.println("Thanks for playing!");

    }
}
