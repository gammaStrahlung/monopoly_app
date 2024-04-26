package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import at.gammastrahlung.monopoly_app.game.gameboard.PropertyColor;

public class PropertyColorTest {
    @ParameterizedTest
    @EnumSource(PropertyColor.class)
    void getColorString(PropertyColor color) {
        assert color.getColorString() != null;
        assertTrue(color.getColorString().matches("^#([a-fA-F0-9]{6})$"));
    }
}
