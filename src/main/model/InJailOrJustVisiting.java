package main.model;

import main.view.InputView;

public class InJailOrJustVisiting extends Square {
    public InJailOrJustVisiting(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        if (player.getInJailDuration() > 0) {
            InputView.displayMessage(player.getName() + " needs to stay in the jail for " + player.getInJailDuration() + " more rounds.\n");
        } else {
            InputView.displayMessage("Just Visiting (jail) square, nothing happens.\n");
        }
    }
    @Override
    public String typeDetailsJson() {
        return "\"type\": \"V\",\n\"details\": {}\n";
    }
}