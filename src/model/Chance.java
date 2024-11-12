package model;
import view.InputView;

import java.util.Random;
public class Chance extends Square {
    private Random random = new Random();
    public Chance(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        int amount;
        if (random.nextBoolean()) {
            amount = random.nextInt(21) * 10;
            player.increaseMoney(amount);
            InputView.displayMessage("Chance square offers " + player.getName() + " " + amount + "HKD.\n");
        } else {
            amount = random.nextInt(31) * 10;
            player.decreaseMoney(amount);
            InputView.displayMessage("Chance square deprives " + player.getName() + " " + amount + "HKD.\n");
        }
    }
    @Override
    public String typeDetailsJson() {
        return "\"details\": {}\n";
    }
}
