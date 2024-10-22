package model;
public class Go extends Square {
    private static final int BONUS = 1500;
    public Go(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        player.increaseMoney(BONUS);
    }
    @Override
    public String typeDetailsJson() {
        return "\"type\": \"G\",\n\"details\": {}\n";
    }
}
