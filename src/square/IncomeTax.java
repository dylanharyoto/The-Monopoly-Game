package square;
import player.Player;

public class IncomeTax extends Square {
    public IncomeTax(String name, int position) {
        super(name, position);
    }
    @Override
    public void takeEffect(Player player) {
        int tax = (int) (player.getMoney() * 0.1);
        tax = (tax / 10) * 10;
        player.decreaseMoney(tax);
    }
}