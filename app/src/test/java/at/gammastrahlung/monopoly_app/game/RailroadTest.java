package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import at.gammastrahlung.monopoly_app.game.gameboard.Railroad;


class RailroadTest {
    Railroad r;

    @BeforeEach
    void initialize() {
        r = new Railroad();
    }

    @Test
    void owner() {
        Player mockPlayer = Mockito.mock(Player.class);
        r.setOwner(mockPlayer);
        assertEquals(mockPlayer, r.getOwner());
    }

    @Test
    void price() {
        r.setPrice(123);
        assertEquals(123, r.getPrice());
    }

    @Test
    void rentPrices() {
        HashMap<String, Integer> map = new HashMap<>();
        r.setRentPrices(map);
        assertEquals(map, r.getRentPrices());
    }
}
