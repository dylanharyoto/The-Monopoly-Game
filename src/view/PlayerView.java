package view;
import model.Property;
import model.Player;
public class PlayerView {
    public void viewPlayer(Player player) {
        System.out.println("Player Information:");
        System.out.println("Name: " + player.getName());
        System.out.println("Money: HKD " + player.getMoney());
        System.out.println("Current Position: " + player.getPosition());
        System.out.println("In Jail Duration: " + player.getInJailDuration());
        if (player.getProperties().isEmpty()) {
            System.out.println("No properties owned.");
        } else {
            System.out.println("Properties:");
            for(Property property : player.getProperties()) {
                System.out.println("- " + property.getName());
            }
        }
    }
}
