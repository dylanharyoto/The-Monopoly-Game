package square;
import java.util.*;
import player.Player;



public class Injail_Visiting extends Square {

    public GoJail(String name, int position) {
        super(name, position);
    }


    @Override
    public void takeEffect(Player player) {
        // need to settle down how and where to store the positions of squares first
        // player.setPosition();
    }
}
