package at.gammastrahlung.monopoly_app.network;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import at.gammastrahlung.monopoly_app.game.Dice;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;

/**
 * Handles messages coming from the server
 */
public class WebSocketHandler {

    /**
     * Main message handler that depending on message.messagePath calls the correct handler.
     *
     * @param message Message from the server
     */
    public void messageReceived(ServerMessage message) {
        switch (message.getMessagePath()) {
            case "create":
                create(message);
                break;
            case "join":
                join(message);
                break;
            case "players":
                players(message);
                break;
            case "start":
                start(message);
                break;
            case "end":
                end(message);
                break;
            case "roll_dice":
                rollDice(message);
                break;
            default:
                Log.w("WebSocket", "Received unknown messagePath from server");
        }
    }

    /**
     * Handles "create" ServerMessages
     *
     * @param message Message from the Server
     */
    private void create(ServerMessage message) {
        GameData gameData = GameData.getGameData();

        if (message.getType() == ServerMessage.MessageType.SUCCESS) {
            // Creating game was successful
            gameData.getGameId().set(Integer.parseInt(message.getMessage()));
            gameData.getPlayers().add(gameData.getPlayer());
            gameData.setGame(message.getGame());
            Log.d("WSHandler", "Create game: " + message.getMessage());
        } else {
            gameData.getGameId().set(-1);
            Log.d("WSHandler", "Could not join game");
        }
    }

    /**
     * Handles "join" ServerMessages
     *
     * @param message Message from the Server
     */
    private void join(ServerMessage message) {
        GameData gameData = GameData.getGameData();

        if (message.getType() == ServerMessage.MessageType.SUCCESS) {

            if (gameData.getPlayer().getId().equals(message.getPlayer().getId())) {
                // This player has joined a game
                gameData.getGameId().set(Integer.parseInt(message.getMessage()));
                gameData.getPlayers().add(gameData.getPlayer());
                gameData.setGame(message.getGame());
                Log.d("WSHandler", "Player joined Game: " + message.getMessage());

                // Sync the player list with the server
                MonopolyClient.getMonopolyClient().getPlayers();
            } else {
                // Other player has joined the game
                gameData.getPlayers().add(message.getPlayer());
                Log.d("WSHandler", "Other player joined game: " + message.getPlayer().getName());
            }

        } else if (message.getType() == ServerMessage.MessageType.ERROR) {
            if (gameData.getPlayer().getId().equals(message.getPlayer().getId())) {
                gameData.getGameId().set(-1); // Could not join game
                Log.d("WSHandler", "Could not join game");
            }
        }
    }

    /**
     * Adds new players in GameData
     *
     * @param message Message from the Server
     */
    private void players(ServerMessage message) {
        GameData gameData = GameData.getGameData();
        Gson gson = new Gson();

        Player[] players = gson.fromJson(message.getMessage(), Player[].class);

        ArrayList<Player> gamePlayers = gameData.getPlayers();

        StringBuilder log = new StringBuilder("All other players:\n");

        // Clear and add this player
        gamePlayers.clear();

        for (Player p : players) {
            if (!p.getId().equals(gameData.getPlayer().getId())) { // Don't add this player
                gamePlayers.add(p);
                log.append(p.getName()).append("\n");
            } else {
                // Update local player with player from Server
                gameData.setPlayer(p);
                gamePlayers.add(p);
            }
        }

        Log.d("WSHandler", log.toString());
    }

    /**
     * Handles when the game has been started
     *
     * @param message Message from the Server
     */
    private void start(ServerMessage message) {
        GameData gameData = GameData.getGameData();

        if (message.getType() == ServerMessage.MessageType.SUCCESS) {
            // Starting the game was successful
            Log.d("WSHandler", "Successfully started game");
            gameData.setGame(message.getGame());
        } else if (message.getType() == ServerMessage.MessageType.ERROR) {
            // Starting the game failed
            Log.d("WSHandler", "Failed to start the game");
            gameData.setGame(null);
        }
    }

    /**
     * Handles when the game has been ended
     *
     * @param message Message from the Server
     */
    private void end(ServerMessage message) {
        GameData gameData = GameData.getGameData();

        if (message.getType() == ServerMessage.MessageType.SUCCESS) {
            // Ending the game was successful
            Log.d("WSHandler", "Successfully ended game");
        } else if (message.getType() == ServerMessage.MessageType.ERROR) {
            // Ending the game failed
            Log.d("WSHandler", "Failed to end the game");
        }

        // Game should be updated anyway. When game has ended, Game.state is set to GameState.ENDED
        gameData.setGame(message.getGame());
    }

    private void rollDice(ServerMessage message) {
        GameData gameData = GameData.getGameData();

        gameData.setDice(message.getGame().getDice());
    }
}
