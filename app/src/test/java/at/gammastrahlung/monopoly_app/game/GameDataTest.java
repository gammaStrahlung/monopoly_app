package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;

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

    @Test
    void currentPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);

        GameData gd = GameData.getGameData();

        gd.setCurrentPlayer(mockPlayer);

        assertEquals(mockPlayer, gd.getCurrentPlayer());
    }

    @Test
    void addLogMessage(){
        GameData gameData = GameData.getGameData();

        String logMessage = "Test log message";
        gameData.addLogMessage(logMessage);
        assertTrue(gameData.getLogMessages().contains(logMessage));
    }

    @Test
    void testMultipleLogMessages() {
        GameData gameData = GameData.getGameData();

        String logMessage1 = "Test log message 1";
        String logMessage2 = "Test log message 2";
        gameData.addLogMessage(logMessage1);
        gameData.addLogMessage(logMessage2);

        assertTrue(gameData.getLogMessages().contains(logMessage1));
        assertTrue(gameData.getLogMessages().contains(logMessage2));
    }

    @Test
    void gameState() {
        GameData gameData = GameData.getGameData();

        assertNull(gameData.getGameState());
        gameData.setGameState(Game.GameState.STARTED);
        assertEquals(Game.GameState.STARTED, gameData.getGameState());
    }

    @Test
    void webSocketConnected() {
        GameData gameData = GameData.getGameData();

        assertFalse(gameData.isWebSocketConnected());
        gameData.setWebSocketConnected(true);
        assertTrue(gameData.isWebSocketConnected());
    }

    @Test
     void testLastMessageTypeGetterAndSetter() {
        GameData gameData = GameData.getGameData();

        // Test setting and getting lastMessageType
        ServerMessage.MessageType testLastMessageType = ServerMessage.MessageType.SUCCESS;
        gameData.setLastMessageType(testLastMessageType);
        assertEquals(testLastMessageType, gameData.getLastMessageType(), "lastMessageType should match the one set.");
    }

    @Test
     void testNullLastMessageType() {
        GameData gameData = GameData.getGameData();
        gameData.setLastMessageType(null);
        assertNull(gameData.getLastMessageType(), "lastMessageType should be null when set to null.");
    }


    @AfterEach
    void cleanup() {
        GameData.reset();
    }
}
