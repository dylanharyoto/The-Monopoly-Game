package model;

import view.InputOutputView;

public class GoJail extends Square {
    public GoJail(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("Go jail square sends " + player.getName() + " to jail.\n");
        player.setInJailDuration(3);
        player.setPosition(6);
    }
    @Override
    public String typeDetailsJson() {
        return "\"details\": {}\n";
    }
}
