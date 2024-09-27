package square;
import player.Player;



public class InJailOrJustVisiting extends Square {

    public InJailOrJustVisiting(int id) {
        super(id);
    }

    public InJailOrJustVisiting(int position, int id) {
        super(position, id);
    }

    @Override
    public void takeEffect(Player player) {
       return;
    }
}
