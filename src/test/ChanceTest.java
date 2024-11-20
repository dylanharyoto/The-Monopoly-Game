package test;
import main.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class ChanceTest {
    private Random mockRandom;
    private Player player;

    private Chance chance;
    private int premoney;
    private boolean increase;
    private boolean decrease;

    @BeforeEach
    void setUp() {
        chance = new Chance(1, "Chance1");
        player = new Player(2, "Bob", 1000, 1);
        increase = false;
        decrease = false;
    }

    @Test
    void testTakeEffectMoney() {
        while(true) {
            premoney = player.getMoney();
            chance.takeEffect(player);
            if(player.getMoney() > premoney){increase = true;}
            if(player.getMoney() < premoney){decrease = true;}
            if(increase && decrease){break;}
        }
    }

    @Test
    void testDetailsInJSON(){chance.detailsInJSON();}
}