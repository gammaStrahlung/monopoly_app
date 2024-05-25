package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;

class GameBoardTest {
    GameBoard b;

    @BeforeEach
    void initialize() {
        b = new GameBoard();
    }

    @Test
    void bank() {
        Player mockPlayer = Mockito.mock(Player.class);

        b.setBank(mockPlayer);
        assertEquals(mockPlayer, b.getBank());
    }

    @Test
    void gameBoard() {
        Field[] fields = new Field[40];

        b.setFields(fields);
        assertEquals(fields, b.getFields());
    }

    @Test
    void boardSize() {
        b.setGameBoardSize(40);
        assertEquals(40, b.getGameBoardSize());
    }

    @Test
    void fullSet() {
        b.setFullSet("full_set");
        assertEquals("full_set", b.getFullSet());
    }

    @Test
    void hotel() {
        b.setHotel("hotel");
        assertEquals("hotel", b.getHotel());
    }
}
