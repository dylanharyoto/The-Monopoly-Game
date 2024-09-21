package square;

import player.Player;

public class Go extends Square {
    private static final int BONUS = 1500;
    public Go(String name, int position) {
        super(name, position);
    }
    @Override
    public void takeEffect(Player player) {
        player.increaseMoney(BONUS);
    }
}
