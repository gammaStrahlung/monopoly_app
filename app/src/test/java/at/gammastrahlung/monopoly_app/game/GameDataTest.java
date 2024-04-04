package at.gammastrahlung.monopoly_app.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameDataTest {
    @Test
    public void getGameData() {
        GameData gameData = GameData.getGameData();

        assertNotNull(gameData);

        // Game id observableInt has been initialized already
        assertNotNull(gameData.getGameId());

        // Player has been initialized already
        assertNotNull(gameData.getPlayer());

        // Players has been initialized already
        assertNotNull(gameData.getPlayers());
    }
}
