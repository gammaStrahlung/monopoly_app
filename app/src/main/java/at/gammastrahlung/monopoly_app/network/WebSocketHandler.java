package at.gammastrahlung.monopoly_app.network;

import android.util.Log;

import androidx.databinding.ObservableArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.gammastrahlung.monopoly_app.game.Game;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;

/**
 * Handles messages coming from the server
 */
public class WebSocketHandler {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Field.class, new FieldDeserializer())
            .create();

    /**
     * Main message handler that depending on message.messagePath calls the correct handler.
     *
     * @param message Message from the server
     */
    public void messageReceived(ServerMessage message) {
        // Set message type
        GameData.getGameData().setLastMessageType(message.getType());

        switch (message.getMessagePath()) {
            case "payment_update":
                handlePaymentUpdate(message);
                break;
            case "create":
            case "join":
                gameChange(message);
                break;
            case "update":
                update(message);
                break;
            case "initiate_round":
                updatePlayerOnTurn(message.getJsonData());
                break;
            case "roll_dice":
                rollDice(message);
                break;
            default:
                Log.w("WebSocket", "Received unknown messagePath from server");
        }
    }
    private void handlePaymentUpdate(ServerMessage message) {
        if (message.getType() == ServerMessage.MessageType.PAYMENT_UPDATE) {
            // Hier Logik für die Zahlungsaktualisierung implementieren
            updatePlayerBalances(message.getJsonData());
            Log.d("WebSocketHandler", "Payment update received: " + message.getJsonData());
        }
    }

    private void updatePlayerBalances(String jsonData) {
        // Annahme: jsonData enthält Informationen über die beteiligten Spieler und deren neue Kontostände
        Player updatedPlayer = gson.fromJson(jsonData, Player.class);
        GameData.getGameData().updatePlayerBalance(updatedPlayer);
        Log.d("WebSocketHandler", "Player balance updated: " + updatedPlayer.getName());
    }

    /**
     * Handles "create" and "join" ServerMessages
     *
     * @param message Message from the Server
     */
    private void gameChange(ServerMessage message) {
        GameData gameData = GameData.getGameData();

        if (message.getType() == ServerMessage.MessageType.SUCCESS) {
            // Creating game was successful
            Game game = gson.fromJson(message.getJsonData(), Game.class);
            gameData.getGameId().set(game.getGameId());
            gameData.setGame(game);

            // Update players
            ObservableArrayList<Player> players = gameData.getPlayers();
            players.clear();
            players.addAll(game.getPlayers());
            game.setPlayers(players);

            gameData.setPlayer(message.getPlayer());
            Log.d("WSHandler", "Create game: " + game.getGameId());
        } else {
            gameData.getGameId().set(-1);
            Log.d("WSHandler", "Could not join game");
        }
    }

    /**
     * Handles a "update" message
     *
     * @param message Message from the server
     */
    private void update(ServerMessage message) {
        // Can't update game if game does not exist
        if (GameData.getGameData().getGame() == null)
            return;

        switch (message.getUpdateType()) {
            case "field":
                updateField(message.getJsonData());
                break;
            case "player":
                updatePlayer(message.getJsonData());
                break;
            case "gameboard":
                updateGameBoard(message.getJsonData());
                break;
            case "gamestate":
                updateGameState(message.getJsonData());
                break;
            case "game":
                updateGame(message.getJsonData());
        }
    }

    private void updatePlayerOnTurn(String json) {
        Player player = gson.fromJson(json, Player.class);
        GameData.getGameData().setCurrentPlayer(player);
    }

    private void updateField(String json) {
        Field f = gson.fromJson(json, Field.class);
        GameData.getGameData().getGame().getGameBoard().getGameBoard()[f.getFieldId()] = f;
    }

    private void updatePlayer(String json) {
        Player p = gson.fromJson(json, Player.class);
        GameData gameData = GameData.getGameData();
        gameData.getPlayers().add(p);

        if (gameData.getPlayer().getId() == p.getId())
            gameData.setPlayer(p);

        if (gameData.getGame().getGameOwner().getId() == p.getId())
            gameData.getGame().setGameOwner(p);

        // Make sure GameData.players and Game.players are the same
        gameData.getGame().setPlayers(gameData.getPlayers());
    }

    private void updateGameBoard(String json) {
        GameBoard gameBoard = gson.fromJson(json, GameBoard.class);
        GameData.getGameData().getGame().setGameBoard(gameBoard);

        // Update game to notify databinding
        GameData.getGameData().setGame(GameData.getGameData().getGame());
    }

    private void updateGameState(String json) {
        Game.GameState state = gson.fromJson(json, Game.GameState.class);
        GameData.getGameData().getGame().setState(state);

        // Update game to notify databinding
        GameData.getGameData().setGame(GameData.getGameData().getGame());
    }

    private void updateGame(String json) {
        Game game = gson.fromJson(json, Game.class);
        GameData gameData = GameData.getGameData();
        gameData.setGame(game);

        // Update players
        ObservableArrayList<Player> players = gameData.getPlayers();
        players.clear();
        players.addAll(game.getPlayers());
        game.setPlayers(players);
    }

    private void rollDice(ServerMessage message) {
        GameData gameData = GameData.getGameData();
        if (gameData.getGame() == null) {
            return;
        }
        gameData.setDice(message.getGame().getDice());
    }
}

