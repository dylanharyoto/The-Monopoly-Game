package main.model;

import main.view.InputOutputView;

public class FreeParking extends Square {
    public FreeParking(int position, String id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        InputOutputView.displayMessage("[FREE PARKING]");
        InputOutputView.displayMessage("Free parking, nothing happens!\n");
    }
    @Override
    public String detailsInJSON() {
        return "\"details\": {}\n";
    }
}
