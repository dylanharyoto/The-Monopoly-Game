package main.view;
import main.model.Property;
import main.model.Player;
public class PlayerView {
    public void viewPlayer(Player player) {
        InputView.displayMessage("Player Information:");
        InputView.displayMessage("Name: " + player.getName());
        InputView.displayMessage("Money: HKD " + player.getMoney());
        InputView.displayMessage("Current Position: " + player.getPosition());
        InputView.displayMessage("In Jail Duration: " + player.getInJailDuration());
        if (player.getProperties().isEmpty()) {
            InputView.displayMessage("No properties owned.");
        } else {
            InputView.displayMessage("Properties:");
            for(Property property : player.getProperties()) {
                InputView.displayMessage("- " + property.getName());
            }
        }
    }
}
