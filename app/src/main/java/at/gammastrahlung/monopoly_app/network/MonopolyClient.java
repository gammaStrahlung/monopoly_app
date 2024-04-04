package at.gammastrahlung.monopoly_app.network;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.dtos.ClientMessage;
import lombok.Getter;

/**
 * Singleton used to send messages to server
 */
public class MonopolyClient {

    @Getter
    private static final MonopolyClient monopolyClient = new MonopolyClient();

    private final WebSocketClient webSocketClient;

    private MonopolyClient() {
        webSocketClient = WebSocketClient.getWebSocketClient();
        webSocketClient.connect(new WebSocketHandler());
    }

    /**
     * Sends a new game message to the server.
     * @param playerName
     */
    public void newGame(String playerName) {
        GameData gameData = GameData.getGameData();
        gameData.getPlayer().setName(playerName);

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("create")
                .player(gameData.getPlayer())
                .build());
    }

    public void joinGame(int gameId, String playerName) {
        GameData gameData = GameData.getGameData();
        gameData.getPlayer().setName(playerName);

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("join")
                .player(gameData.getPlayer())
                .message(String.valueOf(gameId))
                .build());

    }

    /**
     * Gets all players of the same game as the player from the server.
     * This is used when joining to sync the players of the game with the server.
     */
    public void getPlayers() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("players")
                .player(gameData.getPlayer())
                .message(null)
                .build());
    }
}
