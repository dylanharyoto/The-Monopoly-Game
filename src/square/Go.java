package square;

import player.Player;

public class Go extends Square {
    private static final int BONUS = 1500;
    public Go(int position) {
        super(position);
    }
    @Override
    public void takeEffect(Player player) {
        player.increaseMoney(BONUS);
    }
}
