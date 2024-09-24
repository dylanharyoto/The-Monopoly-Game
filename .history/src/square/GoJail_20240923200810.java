package square;

import square.Square;
import java.util.*;
import player.Player;

public class GoJail extends Square {
    private static final int BONUS = 1500;
    public GoJail(String name, int position) {
        super(name, position);
    }
    @Override
    public void takeEffect(Player player) {
        
        player.setPosition()
    }
}
