package model;
public class IncomeTax extends Square {
    public IncomeTax(int id) {
        super(id);
    }
    public IncomeTax(int position, int id) {
        super(position, id);
    }
    @Override
    public void takeEffect(Player player) {
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        int currentMoney = player.getMoney();
        if(currentMoney <= 0) {
            return;
        }
        int tax = (int) (currentMoney * 0.1);
        tax = (tax / 10) * 10;
        player.decreaseMoney(tax);
    }
}
