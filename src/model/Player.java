package model;
import java.util.Random;
import java.util.ArrayList;
public class Player {
    private int id;
    private String name;
    private int money;
    private int position;
    private int inJailDuration = 0;
    private boolean status;
    private final ArrayList<Property> properties;
    public Player(int id, String name, int money, int position){
        this.id = id;
        this.name = name;
        this.money = money;
        this.position = position;
        this.status = true;
        this.properties = new ArrayList<>();
    }
    public void rollDice() {
        Random random = new Random();
        int dice1 = random.nextInt(4) + 1;
        int dice2 = random.nextInt(4) + 1;
        int total = dice1 + dice2;
        if(this.inJailDuration > 0 && dice1 != dice2) {
            this.inJailDuration -= 1;
            return;
        };
        int newPosition = (this.position + total - 1) % 20 + 1;
        setPosition(newPosition);
    }
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public void decreaseMoney(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount to decrease cannot be negative,");
        }
        this.money -= amount;
    }
    public void increaseMoney(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount to increaae cannot be negative,");
        }
        this.money += amount;
    }
    public int getMoney(){
        return this.money;
    }
    public void setInJailDuration(int duration) {
        if(duration < 0) {
            throw new IllegalArgumentException("Jail duration cannot be negative.");
        }
        this.inJailDuration = duration;
    }
    public int getInJailDuration() {
        return this.inJailDuration;
    }
    public void addProperty(Property property) {
        if(property == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }
        this.properties.add(property);
    }
    public ArrayList<Property> getProperties() {
        return this.properties;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition (int position) {
        if(position < 1 || position > 20) {
            throw new IllegalArgumentException("Position must be between 1 and 20");
        }
        this.position = position;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
