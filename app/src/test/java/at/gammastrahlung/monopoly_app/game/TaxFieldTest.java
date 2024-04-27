package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import at.gammastrahlung.monopoly_app.game.gameboard.TaxField;


class TaxFieldTest {
    @Test
    void toPay() {
        TaxField t = new TaxField();
        t.setToPay(123);
        assertEquals(123, t.getToPay());
    }
}
