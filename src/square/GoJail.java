package square;
import java.util.*;
import player.Player;



public class GoJail extends Square {
    private static final int BONUS = 1500;
    public GoJail(int position) {
        super(position);
    }

    @Override
    public void takeEffect(Player player) {
        // need to settle down how and where to store the positions of squares first
        // player.setPosition();
    }
}
