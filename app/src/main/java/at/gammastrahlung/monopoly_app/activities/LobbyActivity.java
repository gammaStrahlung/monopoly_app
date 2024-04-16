package at.gammastrahlung.monopoly_app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.adapters.PlayerAdapter;
import at.gammastrahlung.monopoly_app.game.Game;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

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
        RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter = new PlayerAdapter(players, this);
        playerList.setAdapter(adapter);

        // Disable "Cancel" and "Start" buttons when player is not gameOwner
        if (!gameData.getGame().getGameOwner().equals(gameData.getPlayer())) {
            Button cancel = findViewById(R.id.button_cancel);
            Button start = findViewById(R.id.button_start);

            cancel.setEnabled(false);
            start.setEnabled(false);
        }

        // Add list change callback
        players.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Player>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ObservableList sender) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                adapter.notifyItemRangeChanged(positionStart, positionStart);
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                adapter.notifyItemRangeInserted(positionStart, itemCount);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                // ItemRangeMoved is not available in RecyclerView.Adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                adapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });

        // Add handlers for game end and start
        LobbyActivity context = this;
        gameData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Game game = GameData.getGameData().getGame();

                if (game.getState() == Game.GameState.PLAYING) { // Game was started
                    // Remove this callback
                    GameData.getGameData().removeOnPropertyChangedCallback(this);

                    // Go to Board
                    Intent intent = new Intent(context, BoardGameActivity.class);
                    startActivity(intent);
                } else if (game.getState() == Game.GameState.ENDED) { // Game was cancelled
                    // Remove this callback
                    GameData.getGameData().removeOnPropertyChangedCallback(this);

                    GameData.getGameData().reset();
                    finish(); // Return to MainActivity
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
    }
}
