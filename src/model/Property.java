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
        InputView.displayMessage("Now, " + player.getName() + " reaches " + this.getName() + ".\n");
        if (this.owner != null && this.owner != player) {
            if(player.getMoney() < this.rent) {
                InputView.displayMessage(player.getName() + " do not have enough money to pay the rent for " + this.getName() + "!");
            }
            else {
                InputView.displayMessage(player.getName() + " paid " + this.getRent() + " to the " + this.getOwner().getName() + " of " + this.getName() + "!");
            }
            player.decreaseMoney(this.rent);
            owner.increaseMoney(this.rent);

        }
        else if (owner == null) {
            if (player.getMoney() < this.price) {
                InputView.displayMessage(player.getName() + " does not have enough money to buy " + this.getName() + "!");
            } else {
                String answer = InputView.inputPrompt(
                "Would you like to:\n" +
                "1. Buy " + this.getName() + "\n" +
                "2. Pass " + this.getName() + "\n", new String[]{"1", "2"}
                );
                switch (answer) {
                    case "1":

                        player.decreaseMoney(this.price);
                        player.addProperty(this);
                        this.setOwner(player);
                        InputView.displayMessage("Thanks for buying " + this.getName() + " for " + this.getPrice() + ", " + player.getName() + "!");
                        break;
                    case "2":
                        InputView.displayMessage("Thanks for visiting " + this.getName() + ", " + player.getName() + "!");
                        break;

                }
            }
        }
    }
    @Override
    public String typeDetailsJson() {
        return String.format("\"type\": \"P\",\n\"details\": {\n\"name\": \"%s\",\n\"price\": %d,\n\"rent\": %d\n}",
                this.name, this.price, this.rent);
    }
}
