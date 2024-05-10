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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lobby), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GameData gameData = GameData.getGameData();
        Game game = gameData.getGame();

        if (game == null) {
            Toast.makeText(this, "Game data is not loaded.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView gameIdText = findViewById(R.id.lobby_gameId);
        RecyclerView playerList = findViewById(R.id.playersList);
        String titleText = getString(R.string.gameID) + ": " + game.getGameId();
        gameIdText.setText(titleText);

        playerList.setLayoutManager(new LinearLayoutManager(this));
        ObservableArrayList<Player> players = game.getPlayers();
        PlayerAdapter adapter = new PlayerAdapter(players, this, false, false);
        playerList.setAdapter(adapter);

        Button cancel = findViewById(R.id.button_cancel);
        Button start = findViewById(R.id.button_start);
        if (!game.getGameOwner().equals(gameData.getPlayer())) {
            cancel.setEnabled(false);
            start.setEnabled(false);
        }

        players.addOnListChangedCallback(new PlayerListChangedCallback(this, adapter));
        gameData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId != BR.game) {
                    return; // Only respond to changes in 'game'
                }

                Game currentGame = GameData.getGameData().getGame(); // Re-fetch to ensure current data
                if (currentGame.getState() == Game.GameState.PLAYING) {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(LobbyActivity.this, BoardActivity.class);
                        startActivity(intent);
                    });
                } else if (currentGame.getState() == Game.GameState.ENDED) {
                    runOnUiThread(() -> finish());
                }
            }
        });
    }

    public void cancelButtonClick(View view) {
        MonopolyClient.getMonopolyClient().endGame();
    }

    public void startButtonClick(View view) {
        MonopolyClient.getMonopolyClient().startGame();
        MonopolyClient.getMonopolyClient().initiateRound();
    }
}