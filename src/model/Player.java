/*
* Player class, for initializing player information and operating basic player movements;
* Attributes: name, money, properties, current_position, in_jail;
* Methods: roll_dice, move, set_money, get_money.
*/
package model;
import java.util.Random;
import java.util.ArrayList;

public class Player {
    private int id;
    //name: String type, for recording player name or identification
    private String name;

    //money: int type, for recording how much money the player owns
    private int money;

    //currentPosition: int type, for recording the current location(square index) of the player
    private int currentPosition;

    //inJailDuration: int type, for recording the remaining round number which the player needs to be held in the 'jail'
    private int inJailDuration = 0;

    //properties: String array type, for recording the property squares that the player occupies
    private ArrayList<Property> properties;

    //Player initialization
    public Player(int id, String name, int money, int currentPosition){
        this.id = id;
        this.name = name;
        this.money = money;
        this.currentPosition = currentPosition;
        this.properties = new ArrayList<>();
    }

    //Player information, print all the attributes of the players
    public void getPlayer(){
        int i = 0;
        System.out.println("Player name " + this.name + "\n");
        System.out.println("Player money " + this.money + "\n");
        System.out.println("Player current position " + this.currentPosition + "\n");
        System.out.println("Player remaining jail turn " + this.inJailDuration + "\n");
        for(Property p : this.properties){
            System.out.println("Player property occupation " + p.getName() +"\n");
        }
    }

    //roll_dice: simulates rolling the dice (two 4-sided dice)
    public void rollDice() {
        Random random = new Random();
        int dice1 = random.nextInt(4) + 1;
        int dice2 = random.nextInt(4) + 1;
        int total = dice1 + dice2;
        if(this.inJailDuration > 0 && dice1 != dice2) {
            this.inJailDuration -= 1;
            return;
        };
        this.currentPosition = (this.currentPosition + total - 1) % 20 + 1;
    }


    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    //set_money: change the money of a player
    public void decreaseMoney(int amount) {
        this.money -= amount;
    }
    
    public void increaseMoney(int amount) {
        this.money += amount;
    }

    //get_money: return the money of a player
    public int getMoney(){
        return (this.money);
    }
    public void setInJailDuration(int duration) {
        this.inJailDuration = duration;
    }
    public int getInJailDuration() {
        return this.inJailDuration;
    }
    public void addProperty(Property property) {
        this.properties.add(property);
    }
    public ArrayList<Property> getProperties() {
        return this.properties;
    }

    public int getCurrentPosition() {return this.currentPosition;}

    public void setPosition (int position){
        this.currentPosition = position;
    }
}
