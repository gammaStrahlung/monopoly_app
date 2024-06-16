package at.gammastrahlung.monopoly_app.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.dtos.ClientMessage;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient {
    private static final int MAX_RECONNECT_COUNT = 5;
    private WebSocket webSocket;

    @Getter
    private static final WebSocketClient webSocketClient = new WebSocketClient();
    private WebSocketClient() {}

    @Setter
    @Getter
    private String webSocketURI;

    private int reconnectCount = 0;

    public void connect(WebSocketHandler messageHandler) {

        if (messageHandler == null)
            throw new IllegalArgumentException("messageHandler can not be null");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(webSocketURI).build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t,
                                  @Nullable Response response) {
                if (t.getMessage() != null)
                    Log.w("WebSocket_failure", t.getMessage());

                // Try to reconnect
                if (reconnectCount < MAX_RECONNECT_COUNT) {
                    reconnectCount++;

                    // Wait for one second
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        if (e.getMessage() != null)
                            Log.w("WebSocket_failure", e.getMessage());
                        else
                            Log.w("WebSocket_failure", "Sleeping for one second failed!");
                    }

                    // Reconnect
                    connect(messageHandler);

                    // Re-join game
                    GameData gameData = GameData.getGameData();
                    if (gameData.getGame() != null) {
                        MonopolyClient.getMonopolyClient().joinGame(gameData.getGameId().get(), gameData.getPlayer().getName());
                    }
                } else {
                    GameData.getGameData().setWebSocketConnected(false);
                }

                super.onFailure(webSocket, t, response);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Gson gson = new GsonBuilder()
                        .create();
                // Pass de-serialized message to messageHandler
                messageHandler.messageReceived(gson.fromJson(text, ServerMessage.class));
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
                // Reset reconnect count
                reconnectCount = 0;
                GameData.getGameData().setWebSocketConnected(true);
            }
        });
    }

    public void sendMessage(ClientMessage message) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

            webSocket.send(gson.toJson(message));
        } catch (Exception e) {
            Log.e("WebSocket", e.getMessage());
        }
    }

    public void close() {
        try {
            webSocket.close(1000, "CLOSE");
        } catch (IllegalArgumentException e) {
            Log.e("WebSocket", e.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }
}
