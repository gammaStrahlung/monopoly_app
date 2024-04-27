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

        b.setGameBoard(fields);
        assertEquals(fields, b.getGameBoard());
    }

    @Test
    void boardSize() {
        b.setGAME_BOARD_SIZE(40);
        assertEquals(40, b.getGAME_BOARD_SIZE());
    }

    @Test
    void fullSet() {
        b.setFULL_SET("full_set");
        assertEquals("full_set", b.getFULL_SET());
    }

    @Test
    void hotel() {
        b.setHOTEL("hotel");
        assertEquals("hotel", b.getHOTEL());
    }
}
