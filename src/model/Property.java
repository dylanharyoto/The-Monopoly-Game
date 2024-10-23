package model;
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
//        else if (owner == null) {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Hi " + player.getName() + ", would you like to buy " + this.getName() + "?");
//            String answer = Main.inputPrompt("Please type \"1\" for yes and \"2\" for no.","1,2",scanner);
//            if (answer.equals("1")) {
//                player.decreaseMoney(this.price);
//                player.addProperty(this);
//                this.setOwner(player);
//                System.out.println("Thanks for buying " + this.getName() + " for " + this.getPrice() + ", " + player.getName() + "!");
//            }
//        }
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
