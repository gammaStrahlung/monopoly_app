package at.gammastrahlung.monopoly_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.UUID;

import at.gammastrahlung.monopoly_app.fragments.JoinGameFragment;
import at.gammastrahlung.monopoly_app.fragments.NewGameFragment;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.WebSocketClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Player UUID and WebSocket URI
        updatePlayerUUID();
        WebSocketClient.getWebSocketClient().setWebSocketURI(getString(R.string.websocket_uri));
    }

    public void startButtonClick(View view) {
        new NewGameFragment().show(getSupportFragmentManager(), "NEW_DIALOG");
    }

    public void joinButtonClick(View view) {
        new JoinGameFragment().show(getSupportFragmentManager(), "JOIN_DIALOG");
    }

    /**
     * Sets the player UUID for the Player in GameData.
     * The UUID is only generated once and then saved, this allows re-joining of
     */
    private void updatePlayerUUID() {
        UUID playerUUID;

        // Get UUID from SharedPreferences
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String uuid = sharedPreferences.getString("playerUUID", null);

        if (uuid == null) {
            // UUID has not yet been set (First app start)
            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

            playerUUID = UUID.randomUUID();

            Log.i("PlayerUUID", "Set player UUID to: " + playerUUID);

            preferenceEditor.putString("playerUUID", playerUUID.toString());
            preferenceEditor.apply();
        } else {
            playerUUID = UUID.fromString(uuid);
        }

        GameData.getGameData().getPlayer().setID(playerUUID);
    }
}