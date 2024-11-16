package model;

import view.InputOutputView;

public class IncomeTax extends Square {
    public IncomeTax(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("[INCOME TAX]");
        if(player == null) {
            InputOutputView.displayMessage("Player cannot be null!");
            return;
        }
        int currentMoney = player.getMoney();
        if(currentMoney <= 0) {
            return;
        }
        int tax = (int) (currentMoney * 0.1);
        tax = (tax / 10) * 10;
        player.decreaseMoney(tax);
        InputOutputView.displayMessage(player.getName() + " needs to pay a tax of " + tax + "HKD (10% of the player's money)!\n");
    }
    @Override
    public String detailsInJSON() {
        return "\"details\": {}\n";
    }

}
