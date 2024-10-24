package model;
import java.util.Random;
public class Chance extends Square {
    private Random random = new Random();
    public Chance(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        int amount = (random.nextInt(50) + 1) * 10;
        if (random.nextBoolean()) {
            player.increaseMoney(amount);
        } else {
            player.decreaseMoney(Math.min(player.getMoney(), amount));
        }
    }
    @Override
    public String typeDetailsJson() {
        return "\"type\": \"C\",\n\"details\": {}\n";
    }
}
