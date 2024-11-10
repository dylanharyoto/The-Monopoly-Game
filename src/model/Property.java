package model;

import view.InputView;

public class Property extends Square {
    private String name;
    private int price;
    private int rent;
    private Player owner;
    public Property(int position, String id, String name, int price, int rent) {
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
            if(player.getMoney() < this.rent) {
                throw new IllegalStateException("Player cannot pay rent. Insufficient funds.");
            }
            player.decreaseMoney(this.rent);
            owner.increaseMoney(this.rent);
        }
        else if (owner == null) {
            String answer = InputView.inputPrompt(
    "Hi " + player.getName() + ", welcome to " + this.getName() + ".\n" +
            "Would you like to:\n" +
            "1. Buy " + this.getName() + "\n" +
            "2. Pass " + this.getName() + "\n" +
            "0. Leave the game", new String[]{"1", "2", "0"}
            );
            switch (answer) {
                case "1" -> {
                    player.decreaseMoney(this.price);
                    player.addProperty(this);
                    this.setOwner(player);
                    InputView.displayMessage("Thanks for buying " + this.getName() + " for " + this.getPrice() + ", " + player.getName() + "!");
                }
                case "2" -> {
                    InputView.displayMessage("Thanks for visiting " + this.getName() + ", " + player.getName() + "!");
                }
                case "0" -> {
                    // to be implemented
                    return;
                }
            }
        }
    }
    @Override
    public String typeDetailsJson() {
        return String.format("\"type\": \"P\",\n\"details\": {\n\"name\": \"%s\",\n\"price\": %d,\n\"rent\": %d\n}",
                this.name, this.price, this.rent);
    }
    public boolean isAvailable() {
        return this.owner == null;
    }
    public void buyProperty(Player player) {
        if(this.owner != null) {
            throw new IllegalStateException("This property is already owned.");
        }
        if(player.getMoney() < this.price) {
            throw new IllegalStateException("Player does not have enough money to buy this property.");
        }
        player.decreaseMoney(this.price);
        player.addProperty(this);
        this.setOwner(player);
    }
}
