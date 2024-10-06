package view;

import java.util.Scanner;

public class GameView {
    private Scanner scanner = new Scanner(System.in);
    public String inputPrompt(String message, String validInputs) {
        String input;
        while(true) {
            System.out.println(message);
            input = scanner.nextLine();
        }
    }
}
