package square;

import player.Player;
public abstract class Square {
    private String name;
    private int position;

    public Square(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public abstract void takeEffect(Player player);

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}