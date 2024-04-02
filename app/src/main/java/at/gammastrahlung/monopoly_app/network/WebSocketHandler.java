package at.gammastrahlung.monopoly_app.network;

import android.util.Log;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;

public class WebSocketHandler {

    public void messageReceived(ServerMessage message) {
        switch (message.getMessagePath()) {
            case "create":
                create(message);
                break;
            case "join":
                join(message);
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
            gameData.setGameId(Integer.parseInt(message.getMessage()));
            gameData.getPlayers().put(gameData.getPlayer().getID(), gameData.getPlayer());
            Log.d("WSHandler", "Create game: " + message.getMessage());
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

            if (gameData.getPlayer().getID().equals(message.getPlayer().getID())) {
                // This player has joined a game
                gameData.setGameId(Integer.parseInt(message.getMessage()));
                gameData.getPlayers().put(gameData.getPlayer().getID(), gameData.getPlayer());
                Log.d("WSHandler", "Player joined Game: " + message.getMessage());
            } else {
                // Other player has joined the game
                gameData.getPlayers().put(message.getPlayer().getID(), message.getPlayer());
                Log.d("WSHandler", "Other player joined game: " + message.getPlayer().getName());
            }

        } else if (message.getType() == ServerMessage.MessageType.ERROR) {
            if (gameData.getPlayer().getID() == message.getPlayer().getID()) {
                gameData.setGameId(-1); // Could not join game
                Log.d("WSHandler", "Other player joined game: " + message.getMessage());
            }
        }
    }
}
