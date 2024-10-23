package model;
public class FreeParking extends Square {
    public FreeParking(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        return;
    }
    @Override
    public String typeDetailsJson() {
        return "\"type\": \"F\",\n\"details\": {}\n";
    }
}
