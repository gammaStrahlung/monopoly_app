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
     * @param playerName Name of the player that will be shown to other players.
     */
    public void newGame(String playerName) {
        GameData gameData = GameData.getGameData();
        gameData.getPlayer().setName(playerName);

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("create")
                .player(gameData.getPlayer())
                .build());
    }

    /**
     * Sends a join game message to the server.
     * @param gameId The ID of the game you want to join.
     * @param playerName Name of the player that will be shown to other players.
     */
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

    /**
     * Sends a start game message to the server.
     * Only the player that has created the game can use this.
     */
    public void startGame() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("start")
                .player(gameData.getPlayer())
                .build());
    }

    /**
     * Sends a end game message to the server.
     * Only the player that has created the game can use this.
     */
    public void endGame() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("end")
                .player(gameData.getPlayer())
                .build());
    }

    /**
     * Sends a message roll_dice message to server.
     * Transfer player which diced
     */
    public void rollDice() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("roll_dice")
                .player(gameData.getPlayer())
                .build());
    }

    public void sendMortgageMessage(boolean mortgage, int propertyId) {
        ClientMessage message = ClientMessage.builder()
                .messagePath(mortgage ? "mortgage" : "unmortgage")
                .propertyId(propertyId)  // Properly use the builder pattern
                .player(GameData.getGameData().getPlayer())
                .build();

        webSocketClient.sendMessage(message);
    }


    public void sendPaymentMessage(int amount, String targetPlayerId) {
        // Accessing game data
        GameData gameData = GameData.getGameData();

        // Building client message
        ClientMessage message = ClientMessage.builder()
                .messagePath("payment")
                .player(gameData.getPlayer())
                .message(String.valueOf(amount))
                .targetPlayerId(targetPlayerId)
                .build();

        // Sending message via WebSocket client
        webSocketClient.sendMessage(message);
    }

}
