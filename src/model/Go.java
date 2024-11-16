package model;

import view.InputOutputView;

public class Go extends Square {
    private static final int BONUS = 1500;
    public Go(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("[GO]");
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        player.increaseMoney(BONUS);
        InputOutputView.displayMessage(player.getName() + " gets 1500HKD from GO!\n");
    }
    @Override
    public String detailsInJSON() {
        return "\"details\": {}\n";
    }
}
