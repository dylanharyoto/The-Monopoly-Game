import controller.GameboardController;
import model.Gameboard;
import model.GameboardManager;
import view.GameboardView;
import view.InputView;


public class Main {
    public static void main(String[] args) {
        Gameboard gameboard = new Gameboard();
        GameboardView gameboardView = new GameboardView();
        GameboardController gameboardController = new GameboardController(gameboard, gameboardView);
        String curdir = System.getProperty("user.dir");
        while(true) {
            String choice = InputView.inputPrompt("""
            Welcome to MonoPolyU!
            Would you like to
            1. Start new game
            2. Load existing game
            3. Design a new map""", new String[]{"1", "2", "3"});
            if(choice.equals("1")) {
                if(gameboardController.newGame()) {
                    InputView.displayMessage("Thanks for playing!");
                    break;
                };
            } else if(choice.equals("2")) {
                String filename = InputView.promptFilename("Please input the json filename here");
                filename = filename.endsWith(".json") ? filename : filename + ".json";
                if(GameboardManager.loadGame(curdir + "/assets/games/" + filename, gameboard)) {
                    gameboardController.startGame();
                    InputView.displayMessage("Thanks for playing!");
                    break;
                }
            } else  {
                if(GameboardController.designMap(curdir + "/assets/maps/" + "defaultMap.json")) {
                    break;
                }
            }

        }

    }
}
