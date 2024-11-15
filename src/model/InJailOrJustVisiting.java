package model;

import view.InputOutputView;

public class InJailOrJustVisiting extends Square {
    public InJailOrJustVisiting(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        if (player.getInJailDuration() > 0) {
            InputOutputView.displayMessage(player.getName() + " needs to stay in the jail for " + player.getInJailDuration() + " more rounds.\n");
        } else {
            InputOutputView.displayMessage("Just Visiting (jail) square, nothing happens.\n");
        }
        return;
    }
    @Override
    public String typeDetailsJson() {
        return "\"details\": {}\n";
    }
}
