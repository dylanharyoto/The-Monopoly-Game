import controller.GameboardController;
import model.Gameboard;
import model.GameboardManager;
import view.GameboardView;
import view.InputOutputView;


public class Main {
    public static void main(String[] args) {
        Gameboard gameboard = new Gameboard();
        GameboardView gameboardView = new GameboardView();
        GameboardController gameboardController = new GameboardController(gameboard, gameboardView);
        String curdir = System.getProperty("user.dir");
        while(true) {
            String choice = InputOutputView.promptInput("""
            Welcome to MonoPolyU!
            Would you like to
            1. Start a new game
            2. Load an existing game
            3. Design a new map""", new String[]{"1", "2", "3"});
            if(choice.equals("1")) {
                if(gameboardController.newGame()) {
                    InputOutputView.displayMessage("Thanks for playing!");
                    break;
                };
            } else if(choice.equals("2")) {
                String filename = InputOutputView.promptFilename("Please input the JSON filename here");
                filename = filename.endsWith(".json") ? filename : filename + ".json";
                if(GameboardManager.loadGame(curdir + "/assets/games/" + filename, gameboard)) {
                    gameboardController.startGame();
                    InputOutputView.displayMessage("Thanks for playing!");
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
