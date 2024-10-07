package model;
public class FreeParking extends Square {
    public FreeParking(int id) {
        super(id);
    }
    public FreeParking(int position, int id) {
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
