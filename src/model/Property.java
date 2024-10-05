package model;
import controller.Main;

import java.util.Scanner;
public class Property extends Square {
    private String name;
    private int price;
    private int rent;
    private Player owner;
    public Property(int id, String name, int price, int rent) {
        super(id);
        this.name = name;
        this.price = price;
        this.rent = rent;
    }
    public Property(int position, int id, String name, int price, int rent) {
        super(position, id);
        this.name = name;
        this.price = price;
        this.rent = rent;
    }
    public String getName() {
        return this.name;
    }
    public int getPrice() {
        return this.price;
    }
    public int getRent() {
        return this.rent;
    }
    public void setOwner(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return this.owner;
    }
    @Override
    public void takeEffect(Player player) {
        if (this.owner != null && this.owner != player) {
            player.decreaseMoney(this.rent);
            owner.increaseMoney(this.rent);
        } else if (owner == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Hi " + player.getName() + ", would you like to buy " + this.getName() + "?");

            String answer = Main.inputPrompt("Please type \"1\" for yes and \"2\" for no.","1,2",scanner);

            if (answer.equals("1")) {
                player.decreaseMoney(this.price);
                player.addProperty(this);
                this.setOwner(player);
                System.out.println("Thanks for buying " + this.getName() + " for " + this.getPrice() + ", " + player.getName() + "!");
            }

        }
    }
}
