package square;
import java.util.*;
import player.Player;



public class Injail_Visiting extends Square {

    public GoJail(String name, int position) {
        super(name, position);
    }


    @Override
    public void takeEffect(Player player) {
       return;
    }
}
