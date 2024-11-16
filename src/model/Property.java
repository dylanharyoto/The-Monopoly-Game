package model;

import view.InputOutputView;

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
    public Player getOwner() {
        return this.owner;
    }
    public void setOwner(Player player) {
        this.owner = player;
    }
    public void setName(String newName) {
        this.name = newName;
    }
    public void setPrice (int newPrice) {
        this.price = newPrice;
    }
    public void setRent (int newRent) {
        this.rent = newRent;
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("[PROPERTY]");
        InputOutputView.displayMessage(player.getName() + " reaches " + this.getName() + ".");
        if (this.owner != null && this.owner != player) {
            if(player.getMoney() < this.rent) {
                InputOutputView.displayMessage(player.getName() + " does not have enough money to pay the rent for " + this.getName() + "!\n");
            } else {
                InputOutputView.displayMessage(player.getName() + " paid " + this.getRent() + " of " + this.getName() + " to " + this.getOwner().getName() +  "!\n");
            }
            player.decreaseMoney(this.rent);
            owner.increaseMoney(this.rent);
        } else if (owner == null) {
            if (player.getMoney() < this.price) {
                InputOutputView.displayMessage(player.getName() + " does not have enough money to buy " + this.getName() + "!\n");
            } else {
                String answer = InputOutputView.promptInput(
                "Would you like to:\n" +
                "1. Buy " + this.getName() + "\n" +
                "2. Pass " + this.getName(), new String[]{"1", "2"}
                );
                if (answer.equals("1")) {
                    player.decreaseMoney(this.price);
                    player.addProperty(this);
                    this.setOwner(player);
                    InputOutputView.displayMessage("Thanks for buying " + this.getName() + " for " + this.getPrice() + ", " + player.getName() + "!\n");
                } else if (answer.equals("2")) {
                    InputOutputView.displayMessage("Thanks for visiting " + this.getName() + ", " + player.getName() + "!\n");
                }
            }
        }
    }
    @Override
    public String detailsInJSON() {
        return String.format("\"details\": {\n\"name\": \"%s\",\n\"price\": %d,\n\"rent\": %d\n}", this.name, this.price, this.rent);
    }
}
