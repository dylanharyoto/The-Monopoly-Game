package model;

import view.InputOutputView;

public class InJailOrJustVisiting extends Square {
    public InJailOrJustVisiting(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("[IN JAIL/JUST VISITING]");
        if (player.getInJailDuration() > 0) {
            InputOutputView.displayMessage(player.getName() + " needs to stay in the jail for " + player.getInJailDuration() + " more rounds!\n");
        } else {
            InputOutputView.displayMessage("Just visiting, nothing happens!\n");
        }
    }
    @Override
    public String detailsInJSON() {
        return "\"details\": {}\n";
    }
}
