package square;

import player.Player;
public abstract class Square {
    private int position;
    private int id;

    public Square(int position, int id) {
        this.position = position;
        this.id = id;
    }

    public abstract void takeEffect(Player player);

    public int getPosition() {
        return this.position;
    }
    public int getId() {
        return this.id;
    }
}