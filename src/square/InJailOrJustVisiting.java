package square;
import java.util.*;
import player.Player;



public class InJailOrJustVisiting extends Square {

    public InJailOrJustVisiting(String name, int position) {
        super(name, position);
    }


    @Override
    public void takeEffect(Player player) {
       return;
    }
}
