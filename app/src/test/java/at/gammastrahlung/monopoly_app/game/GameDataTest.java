package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

class GameDataTest {
    @Test
    void getGameData() {
        GameData gameData = GameData.getGameData();

        assertNotNull(gameData);

        // Game id observableInt has been initialized already
        assertNotNull(gameData.getGameId());

        // Player has been initialized already
        assertNotNull(gameData.getPlayer());

        // Players has been initialized already
        assertNotNull(gameData.getPlayers());
    }

    @Test
    void player() {
        Player mockPlayer = Mockito.mock(Player.class);
        Mockito.when(mockPlayer.getId()).thenReturn(UUID.randomUUID());

        GameData gd = GameData.getGameData();

        gd.setPlayer(mockPlayer);
        assertEquals(mockPlayer, gd.getPlayer());
    }

    @Test
    void game() {
        Game mockGame = Mockito.mock(Game.class);

        GameData gd = GameData.getGameData();

        gd.setGame(mockGame);
        assertEquals(mockGame, gd.getGame());
    }

    @Test
    void reset() {
        GameData gameData1 = GameData.getGameData();

        GameData.reset();

        GameData gameData2 = GameData.getGameData();

        assertNotEquals(gameData1, gameData2);
    }

    @Test
    void dice(){
        Dice mockDice = Mockito.mock(Dice.class);

        GameData gd = GameData.getGameData();

        gd.setDice(mockDice);
        assertEquals(mockDice,gd.getDice());
    }

    @AfterEach
    void cleanup() {
        GameData.reset();
    }
}
