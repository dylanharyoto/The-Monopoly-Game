package model;

import view.InputView;

public class IncomeTax extends Square {
    public IncomeTax(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        int currentMoney = player.getMoney();
        if(currentMoney <= 0) {
            return;
        }
        int tax = (int) (currentMoney * 0.1);
        tax = (tax / 10) * 10;
        player.decreaseMoney(tax);
        InputView.displayMessage("Tax square deprives " + player.getName() + " " + tax + "HKD (10% of the player's money).\n");
    }
    @Override
    public String typeDetailsJson() {
        return "\"details\": {}\n";
    }

}
