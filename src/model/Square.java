package model;

public abstract class Square {
    private int position;
    private String id;
    public Square(int position, String id) {
        this.position = position;
        this.id = id;
    }
    public abstract void takeEffect(Player player);
    public abstract String detailsInJSON();
    public void setPosition(int position) {
        if(position < 1 || position > 20) {
            throw new IllegalArgumentException("Position must be between 1 and 20");
        }
        this.position = position;
    }
    public int getPosition() {
        return this.position;
    }
    public String getId() {
        return this.id;
    }
}