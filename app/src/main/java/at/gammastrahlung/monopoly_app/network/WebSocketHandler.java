package at.gammastrahlung.monopoly_app.network;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.UUID;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
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
            case "players":
                players(message);
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

                // Update player list
                MonopolyClient.getMonopolyClient().getPlayers();
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

    /**
     * Adds new players in GameData
     *
     * @param message Message from the Server
     */
    private void players(ServerMessage message) {
        GameData gameData = GameData.getGameData();
        Gson gson = new Gson();

        Player[] players = gson.fromJson(message.getMessage(), Player[].class);

        HashMap<UUID, Player> gamePlayers = gameData.getPlayers();

        StringBuilder log = new StringBuilder("All other players:\n");

        for (Player p : players) {
            if (!p.getID().equals(gameData.getPlayer().getID())) { // Don't add this player
                gamePlayers.put(p.getID(), p);
                log.append(p.getName()).append("\n");
            }
        }

        Log.d("WSHandler", log.toString());
    }
}
