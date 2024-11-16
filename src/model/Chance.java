package model;
import view.InputOutputView;

import java.util.Random;
public class Chance extends Square {
    private Random random = new Random();
    public Chance(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("[CHANCE]");
        int amount;
        if (random.nextBoolean()) {
            amount = random.nextInt(21) * 10;
            player.increaseMoney(amount);
            InputOutputView.displayMessage(player.getName() + " gets " + amount + "HKD from CHANCE!\n");
        } else {
            amount = random.nextInt(31) * 10;
            player.decreaseMoney(amount);
            InputOutputView.displayMessage(player.getName() + " needs to pay " + amount + "HKD to CHANCE!\n");
        }
    }
    @Override
    public String detailsInJSON() {
        return "\"details\": {}\n";
    }
}
