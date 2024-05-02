package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DiceTest {

    @Test
    void newDiceTest(){
        Dice dice = Mockito.mock(Dice.class);

        assertNotNull(dice);
    }

    @Test
    void diceConstructorTest(){
        Dice dice = new Dice(1,2);

        assertEquals(1, dice.getValue1());
        assertEquals(2, dice.getValue2());
    }
}
