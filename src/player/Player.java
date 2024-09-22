/*
* Player class, for initializing player information and operating basic player movements;
* Attributes: name, money, properties, current_position, in_jail;
* Methods: roll_dice, move, set_money, get_money.
*/
package player;
import square.Property;
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
    public Player(String name, int money, int currentPosition){
        this.name = name;
        this.money = money;
        this.currentPosition = currentPosition;
    }

    //Player information, print all the attributes of the players
    public void getPlayer(){
        int i = 0;
        System.out.println("Player name " + this.name + "\n");
        System.out.println("Player money " + this.money + "\n");
        System.out.println("Player current position " + this.currentPosition + "\n");
        System.out.println("Player remaining jail turn " + this.inJailDuration + "\n");
        while(this.properties.get(i) != null && i < 20) {
            System.out.println("Player property occupation " + this.properties.get(i) +"\n");
            i++;
        }
    }

    //roll_dice: simulates rolling the dice (two 4-sided dice)
    public int[] rollDice(){
        Random rand = new Random();
        int[] res = new int[3];
        res[0] = rand.nextInt(4) + 1;
        res[1] = rand.nextInt(4) + 1;
        res[2] = res[0] + res[1];
        return res;
    }

    public int getCurrentPosition(){
        return this.currentPosition;
    }

    //move: moves the player around the board
    public void move(){
        this.currentPosition += rollDice()[3];
        if(this.currentPosition > 20){currentPosition -= 20;}
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
        System.out.println(this.money);
        return (this.money);
    }
    public void setInJailDuration(int duration) {
        this.inJailDuration = duration;
    }
    public int getInJailDuration() {
        return this.inJailDuration;
    }
    public void addProperties(Property property) {
        this.properties.add(property);
    }
    public ArrayList<Property> getProperties() {
        return this.properties;
    }
}
