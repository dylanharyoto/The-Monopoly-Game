package square;

import player.Player;

public class Go extends Square {
    private static final int BONUS = 1500;
    public Go(int position, int id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        player.increaseMoney(BONUS);
    }
}
