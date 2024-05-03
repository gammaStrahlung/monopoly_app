package at.gammastrahlung.monopoly_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.adapters.PlayerAdapter;
import at.gammastrahlung.monopoly_app.adapters.PlayerListChangedCallback;
import at.gammastrahlung.monopoly_app.game.Game;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;

public class LobbyActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lobby), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GameData gameData = GameData.getGameData();

        TextView gameIdText = findViewById(R.id.lobby_gameId);
        RecyclerView playerList = findViewById(R.id.playersList);

        String titleText = getString(R.string.gameID) + ": " + gameData.getGameId().get();

        // Set game id text
        gameIdText.setText(titleText);


        // Initialize "playerList" RecyclerView
        playerList.setLayoutManager(new LinearLayoutManager(this));
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();
        RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter = new PlayerAdapter(players, this, false, false);
        playerList.setAdapter(adapter);

        // Disable "Cancel" and "Start" buttons when player is not gameOwner
        if (!gameData.getGame().getGameOwner().equals(gameData.getPlayer())) {
            Button cancel = findViewById(R.id.button_cancel);
            Button start = findViewById(R.id.button_start);

            cancel.setEnabled(false);
            start.setEnabled(false);
        }

        LobbyActivity activity = this;

        // Add list change callback
        players.addOnListChangedCallback(new PlayerListChangedCallback(activity, adapter));

        // Add handlers for game end and start
        gameData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId != BR.game)
                    return; // Something other then game has changed -> Ignore

                Game game = GameData.getGameData().getGame();

                if (GameData.getGameData().getLastMessageType() == ServerMessage.MessageType.ERROR) { // Starting the game was not successful
                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, R.string.startGame_fail, Toast.LENGTH_LONG).show());
                } else if (game.getState() == Game.GameState.PLAYING) { // Game was started
                    // Remove this callback
                    GameData.getGameData().removeOnPropertyChangedCallback(this);

                    activity.runOnUiThread(() -> {
                        // Go to Board
                        Intent intent = new Intent(activity, BoardActivity.class);
                        startActivity(intent);
                    });
                } else if (game.getState() == Game.GameState.ENDED) { // Game was cancelled
                    // Remove this callback
                    GameData.getGameData().removeOnPropertyChangedCallback(this);

                    GameData.reset();
                    activity.runOnUiThread(() -> {
                        finish(); // Return to MainActivity
                    });
                }
            }
        });
    }

    // Cancel button ends the game and returns to main menu
    public void cancelButtonClick(View view) {
        MonopolyClient.getMonopolyClient().endGame();
    }

    // Start button starts the game and opens BoardGameActivity
    public void startButtonClick(View view) {
        MonopolyClient.getMonopolyClient().startGame();
        MonopolyClient.getMonopolyClient().initiateRound();
    }
}
