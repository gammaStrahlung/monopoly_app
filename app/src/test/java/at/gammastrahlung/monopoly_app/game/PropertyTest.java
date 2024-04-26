package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import java.util.HashMap;

import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.PropertyColor;


class PropertyTest {
    Property p;

    @BeforeEach
    void initialize() {
        p = new Property();
    }

    @Test
    void price() {

    }

    @Test
    void owner() {
        Player mockPlayer = Mockito.mock(Player.class);
        p.setOwner(mockPlayer);
        assertEquals(mockPlayer, p.getOwner());
    }

    @ParameterizedTest
    @EnumSource(PropertyColor.class)
    void price(PropertyColor color) {
        p.setColor(color);
        assertEquals(color, p.getColor());
    }

    @Test
    void rentPrices() {
        HashMap<Object, Integer> map = new HashMap<>();
        p.setRentPrices(map);
        assertEquals(map, p.getRentPrices());
    }

    @Test
    void mortgageValue() {
        p.setMortgageValue(111);
        assertEquals(111, p.getMortgageValue());
    }

    @Test
    void houseCost() {
        p.setHouseCost(123);
        assertEquals(123, p.getHouseCost());
    }

    @Test
    void hotelCost() {
        p.setHotelCost(456);
        assertEquals(456, p.getHotelCost());
    }

    @Test
    void houseCount() {
        p.setHouseCount(890);
        assertEquals(890, p.getHouseCount());
    }
}
