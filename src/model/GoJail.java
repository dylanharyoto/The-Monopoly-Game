package model;

import view.InputView;

public class GoJail extends Square {
    public GoJail(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputView.displayMessage("Go jail square sends " + player.getName() + " to jail.\n");
        player.setInJailDuration(3);
        player.setPosition(6);
    }
    @Override
    public String typeDetailsJson() {
        return "\"type\": \"J\",\n\"details\": {}\n";
    }
}
