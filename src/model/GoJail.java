package model;
public class GoJail extends Square {
    public GoJail(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        // player.setPosition();
    }
    @Override
    public String typeDetailsJson() {
        return "\"type\": \"J\",\n\"details\": {}\n";
    }
}
