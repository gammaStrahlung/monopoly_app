package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import androidx.databinding.ObservableArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;

class GameTest {
    @Test
    void newGame() {
        Player mockPlayer = Mockito.mock(Player.class);
        Mockito.when(mockPlayer.getId()).thenReturn(UUID.randomUUID());

        Dice mockDice = Mockito.mock(Dice.class);

        GameBoard mockBoard = Mockito.mock(GameBoard.class);

        Game g1 = new Game();

        assertEquals(0, g1.getGameId()); // Default int value
        assertNull(g1.getState());
        assertNull(g1.getGameOwner());

        ObservableArrayList<Player> players = new ObservableArrayList<>();
        players.add(mockPlayer);

        Game g2 = new Game(1, Game.GameState.STARTED, mockPlayer, mockBoard, players, mockDice, null, 10, 1);

        assertEquals(1, g2.getGameId());
        assertEquals(Game.GameState.STARTED, g2.getState());
        assertEquals(mockPlayer, g2.getGameOwner());
        assertEquals(mockDice, g2.getDice());
        assertNull(g2.getWinningPlayer());
        assertEquals(10, g2.getRoundAmount());
        assertEquals(1, g2.getCurrentRound());
    }

    @Test
    void gameId() {
        Game g = new Game();

        g.setGameId(123);
        assertEquals(123, g.getGameId());
    }

    @Test
    void state() {
        Game g = new Game();

        g.setState(Game.GameState.STARTED);
        assertEquals(Game.GameState.STARTED, g.getState());

        g.setState(Game.GameState.PLAYING);
        assertEquals(Game.GameState.PLAYING, g.getState());

        g.setState(Game.GameState.ENDED);
        assertEquals(Game.GameState.ENDED, g.getState());
    }

    @Test
    void gameOwner() {
        Player mockPlayer = Mockito.mock(Player.class);
        Mockito.when(mockPlayer.getId()).thenReturn(UUID.randomUUID());

        Game g = new Game();

        g.setGameOwner(mockPlayer);
        assertEquals(mockPlayer, g.getGameOwner());
    }

    @Test
    void gameBoard() {
        Game g = new Game();

        GameBoard mockBoard = Mockito.mock(GameBoard.class);

        g.setGameBoard(mockBoard);
        assertEquals(mockBoard, g.getGameBoard());
    }

    @Test
    void winningPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);

        Game g = new Game();

        g.setWinningPlayer(mockPlayer);
        assertEquals(mockPlayer, g.getWinningPlayer());
    }

    @Test
    void roundAmount() {
        Game g = new Game();

        g.setRoundAmount(5);
        assertEquals(5, g.getRoundAmount());
    }

    @Test
    void currentRound() {
        Game g = new Game();

        g.setCurrentRound(5);
        assertEquals(5, g.getCurrentRound());
    }
}
