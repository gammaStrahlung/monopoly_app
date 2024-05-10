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

    /**
     * Sends a initiate round message to the server.
     */
    public void initiateRound() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("initiate_round")
                .player(gameData.getPlayer())
                .build());
    }

    public void endCurrentPlayerTurn(){
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("end_current_player_turn")
                .player(gameData.getPlayer())
                .build());
    }

    /**
     * Sends a payment message to the server.
     * @param amount The amount to be paid.
     * @param targetPlayerId The ID of the player to receive the payment.
     */
    // New method in the MonopolyClient class
    public void sendPaymentMessage(int amount, String targetPlayerId) {
        // Accessing game data
        GameData gameData = GameData.getGameData();

        // Building client message
        ClientMessage message = ClientMessage.builder()
                .messagePath("payment")
                .player(gameData.getPlayer())
                .message(String.valueOf(amount))
                .targetPlayerId(targetPlayerId) // You may need to add this field to the ClientMessage.
                .build();

        // Sending message via WebSocket client
        webSocketClient.sendMessage(message);
    }


    /**
     * Sends a message to start an auction for a specific property.
     *
     * @param propertyId The ID of the property for which the auction is to start.
     */
    public void startAuction(int propertyId) {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("start_auction")
                .message(String.valueOf(propertyId))
                .player(GameData.getGameData().getPlayer())
                .build());
    }

    /**
     * Sends a bid for the current auction.
     *
     * @param bid The bid amount.
     */
    public void placeBid(int bid) {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("place_bid")
                .message(String.valueOf(bid))
                .player(GameData.getGameData().getPlayer())
                .build());
    }

    /**
     * Sends a message to finalize the current auction.
     */
    public void finalizeAuction() {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("finalize_auction")
                .player(GameData.getGameData().getPlayer())
                .build());
    }

    public void moveAvatar(){
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("move_avatar")
                .player(gameData.getPlayer())
                .build());
    }
}
