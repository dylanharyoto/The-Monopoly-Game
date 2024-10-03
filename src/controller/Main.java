package controller;

import game.Gameboard;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Welcome to the game");
        //Gameboard gameboard = new Gameboard();
        String[] a = ",".split(",");
        System.out.println(a.length);
    }

    /**
     * Given the prompt and available options, return the legal input (answer) as string.
     */
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
    public static String inputPromptTwo(String prompt, String options) {
        String[] optionsArray = options.split(",");
        Scanner scanner = new Scanner(System.in);
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
        scanner.close();
        return answer;
    }

    private static boolean contains(String[] array, String element) {
        for(String str : array) if(str.equals(element)) return true;
        return false;
    }
}
