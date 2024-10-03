package model;

public abstract class Square {
    private int position;
    private int id;

    public Square(int id) {
        this.id = id;
    }
    public Square(int position, int id) {
        this.position = position;
        this.id = id;
    }

    public abstract void takeEffect(Player player);
    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return this.position;
    }
    public int getId() {
        return this.id;
    }
}