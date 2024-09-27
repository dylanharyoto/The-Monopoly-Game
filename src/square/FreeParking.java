package square;

import player.Player;

public class FreeParking extends Square {
    public FreeParking(int position, int id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        return;
    }
}
