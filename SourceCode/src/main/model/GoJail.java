package main.model;

import main.view.InputOutputView;

public class GoJail extends Square {
    public GoJail(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("[GO TO JAIL]");
        player.setInJailDuration(3);
        player.setPosition(6);
        InputOutputView.displayMessage(player.getName() + " is sent to jail!\n");
    }
    @Override
    public String detailsInJSON() {
        return "\"details\": {}\n";
    }
}
