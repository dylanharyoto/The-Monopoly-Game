package square;
import player.Player;

public class GoJail extends Square {
    public GoJail(int position, int id) {
        super(position, id);
    }

    @Override
    public void takeEffect(Player player) {
        // need to settle down how and where to store the positions of squares first
        // player.setPosition();
    }
}
