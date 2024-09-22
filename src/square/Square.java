package square;

import player.Player;
public abstract class Square {
    private int position;

    public Square(int position) {
        this.position = position;
    }

    public abstract void takeEffect(Player player);

    public int getPosition() {
        return position;
    }
}