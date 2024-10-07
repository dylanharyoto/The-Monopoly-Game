package model;
public class GoJail extends Square {
    public GoJail(int id) {
        super(id);
    }
    public GoJail(int position, int id) {
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
