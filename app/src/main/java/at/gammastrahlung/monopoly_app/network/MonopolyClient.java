package at.gammastrahlung.monopoly_app.network;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.dtos.ClientMessage;
import lombok.Getter;

/**
 * Singleton used to send messages to server
 */
public class MonopolyClient {

    @Getter
    private static MonopolyClient monopolyClient = new MonopolyClient();

    private final WebSocketClient webSocketClient;

    private MonopolyClient() {
        webSocketClient = WebSocketClient.getWebSocketClient();
        webSocketClient.connect(new WebSocketHandler());
    }

    public static void reset() {
        monopolyClient = new MonopolyClient();
    }

    /**
     * Sends a new game message to the server.
     *
     * @param playerName Name of the player that will be shown to other players.
     * @param roundAmount The amount of rounds until the game ends.
     */
    public void newGame(String playerName, int roundAmount) {
        GameData gameData = GameData.getGameData();
        gameData.getPlayer().setName(playerName);

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("create")
                .player(gameData.getPlayer())
                .message(String.valueOf(roundAmount))
                .build());
    }

    /**
     * Sends a join game message to the server.
     *
     * @param gameId     The ID of the game you want to join.
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

    public void endCurrentPlayerTurn() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("end_current_player_turn")
                .player(gameData.getPlayer())
                .build());
    }

    public void moveAvatar() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("move_avatar")
                .player(gameData.getPlayer())
                .build());
    }

    public void moveAvatarAfterCheating() {
        GameData gameData = GameData.getGameData();

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("move_avatar_cheating")
                .player(gameData.getPlayer())
                .build());
    }

    /**
     * Sends the new selected values
     *
     * @param value1
     * @param value2
     */
    public void cheating(int value1, int value2) {
        GameData gameData = GameData.getGameData();

        gameData.getDice().setValue1(value1);
        gameData.getDice().setValue2(value2);

        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("cheating")
                .message(String.valueOf(value1 + value2))
                .player(gameData.getPlayer())
                .build());
    }

    /**
     * Sends a game_state message to the server.
     *
     * @param gameId The ID of the game
     */
    public void getGameState(int gameId) {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("game_state")
                .message(String.valueOf(gameId))
                .player(GameData.getGameData().getPlayer())
                .build());
    }

    /**
     * Sends a report_cheat message to the server.
     *
     * @param index The index of the accused Player
     */
    public void reportCheat(int index) {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("report_cheat")
                .player(GameData.getGameData().getPlayer())
                .message(String.valueOf(index))
                .build());
    }

    /**
     * Sends a report_wrong_cheat message to the server
     */
    public void reportPenalty() {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("report_penalty")
                .player(GameData.getGameData().getPlayer())
                .build());
    }

    /**
     * Sends a award_report message to the server
     */
    public void reportAward() {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("report_award")
                .player(GameData.getGameData().getPlayer())
                .build());
    }

    /**
     * Sends a buy_field message to the server
     * @param fieldId the id of the field
     */
    public void buyField(int fieldId) {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("buy_field")
                .player(GameData.getGameData().getPlayer())
                .message(String.valueOf(fieldId))
                .build());
    }

    public void buildHouse(int fieldIndex) {
        webSocketClient.sendMessage(ClientMessage.builder()
                .messagePath("build_property")
                .player(GameData.getGameData().getPlayer())
                .message(String.valueOf(fieldIndex))
                .build());
    }
}
