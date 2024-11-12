package model;

import view.InputView;

public class FreeParking extends Square {
    public FreeParking(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputView.displayMessage("Free Parking square, nothing happens.\n");
        return;
    }
    @Override
    public String typeDetailsJson() {
        return "\"details\": {}\n";
    }
}
