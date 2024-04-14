package at.gammastrahlung.monopoly_app.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.adapters.PlayerAdapter;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;

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

        TextView gameIdText = findViewById(R.id.lobby_gameId);
        RecyclerView playerList = findViewById(R.id.playersList);

        String titleText = getString(R.string.gameID) + ": " + GameData.getGameData().getGameId().get();

        // Set game id text
        gameIdText.setText(titleText);


        // Initialize "playerList" RecyclerView
        playerList.setLayoutManager(new LinearLayoutManager(this));
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();
        RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter = new PlayerAdapter(players, this);
        playerList.setAdapter(adapter);

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
    }

    // Cancel button ends the game and returns to main menu
    public void cancelButtonClick(View view) {

    }

    // Start button starts the game and opens BoardGameActivity
    public void startButtonClick(View view) {

    }
}
