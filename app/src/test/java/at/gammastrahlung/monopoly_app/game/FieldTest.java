package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.FieldType;

class FieldTest {

    private Field f;

    @BeforeEach
    void initialize() {
        f = new Field();
    }

    @Test
    void fieldId() {
        f.setFieldId(1);
        assertEquals(1, f.getFieldId());
    }

    @Test
    void name() {
        f.setName("name");
        assertEquals("name", f.getName());
    }

    @ParameterizedTest
    @EnumSource(FieldType.class)
    void type(FieldType type) {
        f.setType(type);
        assertEquals(type, f.getType());
    }

    @Test
    void boardName() {
        f.setBoardName("boardName");
        assertEquals("boardName", f.getBoardName());
    }
}
