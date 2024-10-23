package model;
public class InJailOrJustVisiting extends Square {
    public InJailOrJustVisiting(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        return;
    }
    @Override
    public String typeDetailsJson() {
        return "\"type\": \"V\",\n\"details\": {}\n";
    }
}
