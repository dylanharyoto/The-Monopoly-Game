package controller;

import model.Gameboard;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Welcome to the game");
        Gameboard gameboard = new Gameboard();
    }

    /**
     * Given the prompt and available options, return the legal input (answer) as string.
     */

    public static String inputPrompt(String prompt, String options, Scanner scanner) {
        String[] optionsArray = options.split(",");
        System.out.println(prompt);
        System.out.print("> ");
        String answer = scanner.next();
        if(optionsArray.length == 0) return answer;
        while (!contains(optionsArray, answer)) {
            System.out.println("Invalid answer! Please double check the available options and retype.");
            System.out.println(prompt);
            System.out.print("> ");
            answer = scanner.next();
        }
        return answer;
    }

    private static boolean contains(String[] array, String element) {
        for(String str : array) if(str.equals(element)) return true;
        return false;
    }

    /*
    public static String inputPrompt(String prompt, ArrayList<String> options) {

        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        System.out.print("> ");
        String answer = scanner.next();
        while (!options.contains(answer)) {
            System.out.println("Invalid answer! Please double check the available options and retype.");
            System.out.println(prompt);
            System.out.print("> ");
            answer = scanner.next();
        }
        scanner.close();
        return answer;
    }
     */
}
