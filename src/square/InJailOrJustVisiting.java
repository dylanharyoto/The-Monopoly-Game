package square;
import java.util.*;
import player.Player;



public class InJailOrJustVisiting extends Square {

    public InJailOrJustVisiting(int position) {
        super(position);
    }


    @Override
    public void takeEffect(Player player) {
       return;
    }
}
