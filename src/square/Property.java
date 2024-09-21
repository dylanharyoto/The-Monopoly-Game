package square;
import player.Player;
import java.util.Scanner;
public class Property extends Square {
    private int price;
    private int rent;
    private Player owner;
    public Property(String name, int position, int price, int rent) {
        super(name, position);
        this.price = price;
        this.rent = rent;
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
            System.out.print("Answer (y/n): ");
            String answer = scanner.next();
            while (!answer.equals("y") && !answer.equals("n")) {
                System.out.println("Please type \"y\" for yes and \"n\" for no");
                System.out.print("Answer (y/n): ");
                answer = scanner.next();
            }
            if (answer.equals("y")) {
                player.decreaseMoney(this.price);
                player.addProperties(this);
                this.setOwner(player);
                System.out.println("Thanks for buying " + this.getName() + " for " + this.getPrice() + ", " + player.getName() + "!");
            }
        }
    }
}
