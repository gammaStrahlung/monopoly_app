package at.gammastrahlung.monopoly_app.activities;

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
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.UUID;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.fragments.JoinGameFragment;
import at.gammastrahlung.monopoly_app.fragments.NewGameFragment;
import at.gammastrahlung.monopoly_app.fragments.ReJoinGameFragment;
import at.gammastrahlung.monopoly_app.game.Game;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;
import at.gammastrahlung.monopoly_app.network.WebSocketClient;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize Player UUID and WebSocket URI
        updatePlayer();
        WebSocketClient.getWebSocketClient().setWebSocketURI(getString(R.string.websocket_uri));

        // Check last game and show dialog when it has not ended
        int lastGameId = sharedPreferences.getInt("gameId", 0);
        if (lastGameId != 0) {
            GameData.getGameData().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (propertyId == BR.gameState) {
                        Game.GameState state = GameData.getGameData().getGameState();
                        if (state == Game.GameState.PLAYING || state == Game.GameState.STARTED) {
                            new ReJoinGameFragment(lastGameId).show(getSupportFragmentManager(), "REJOIN_DIALOG");
                        } else {
                            // Game does not exit or it has ended -> remove gameId
                            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                            preferenceEditor.remove("gameId");
                            preferenceEditor.apply();
                        }
                        GameData.getGameData().removeOnPropertyChangedCallback(this);
                    }
                }
            });

            MonopolyClient.getMonopolyClient().getGameState(lastGameId);
        }
    }

    public void startButtonClick(View view) {
        new NewGameFragment().show(getSupportFragmentManager(), "NEW_DIALOG");
    }

    public void joinButtonClick(View view) {
        new JoinGameFragment().show(getSupportFragmentManager(), "JOIN_DIALOG");
    }

    /**
     * Sets the player UUID and the name for the Player in GameData.
     * The UUID is only generated once and then saved, this allows re-joining of the game
     */
    private void updatePlayer() {
        UUID playerUUID;

        // Get UUID from SharedPreferences
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

        GameData.getGameData().getPlayer().setId(playerUUID);

        // Set player name
        GameData.getGameData().getPlayer().setName(sharedPreferences.getString("playerName", ""));
    }
}