package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DiceTest {

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

    @Test
    void setDiceTest(){
        Dice dice = new Dice(3,2);

        assertEquals(3, dice.getValue1());
        assertEquals(2, dice.getValue2());

        dice.setValue1(4);
        dice.setValue2(6);

        assertEquals(4, dice.getValue1());
        assertEquals(6, dice.getValue2());
    }
}
