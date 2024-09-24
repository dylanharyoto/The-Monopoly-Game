import game.Gameboard;
import player.Player;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the game");
        Gameboard gameboard = new Gameboard();
        Player player1 = new Player(1,"1",1,1);
        Player player2 = new Player(2,"2",2,2);
        gameboard.joinPlayer(player1);
        gameboard.joinPlayer(player2);
        gameboard.startGame();
    }
}
