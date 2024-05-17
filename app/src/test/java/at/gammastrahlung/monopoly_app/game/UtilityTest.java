package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import at.gammastrahlung.monopoly_app.game.gameboard.Utility;


class UtilityTest {
    Utility u;

    @BeforeEach
    void initialize() {
        u = new Utility();
    }

    @Test
    void owner() {
        Player mockPlayer = Mockito.mock(Player.class);

        u.setOwner(mockPlayer);
        assertEquals(mockPlayer, u.getOwner());
    }

    @Test
    void toPay() {
        u.setToPay(123);
        assertEquals(123, u.getToPay());
    }

    @Test
    void price() {
        u.setPrice(456);
        assertEquals(456, u.getPrice());
    }

    @Test
    void mortgage() {
        u.setMortgage(789);
        assertEquals(789, u.getMortgage());
    }
}
