package square;
import java.util.*;
import player.Player;



public class Injail_Visiting extends Square {

    public GoJail(String name, int position) {
        super(name, position);
    }


    @Override
    public void takeEffect(Player player) {
        // need to collaborate about what to do after gojail takes effect.
        int inJailDuration = player.getInJailDuration();
        if (inJailDuration == 0) {
            return;
        }
    
    }
}
