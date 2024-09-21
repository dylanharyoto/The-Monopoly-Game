package square;

import player.Player;

public class FreeParking extends Square {
    public FreeParking(String name, int position) {
        super(name, position);
    }
    @Override
    public void takeEffect(Player player) {
        return;
    }
}
