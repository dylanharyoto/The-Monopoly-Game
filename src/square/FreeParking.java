package square;

import player.Player;

public class FreeParking extends Square {
    public FreeParking(int position) {
        super(position);
    }
    @Override
    public void takeEffect(Player player) {
        return;
    }
}
