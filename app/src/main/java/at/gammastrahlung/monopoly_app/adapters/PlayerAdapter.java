package at.gammastrahlung.monopoly_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    ObservableArrayList<Player> players;

    public PlayerAdapter(ObservableArrayList<Player> players, Context context) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_view, parent, false);

        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {

        Player thisPlayer = GameData.getGameData().getPlayer();
        Player gameOwner = GameData.getGameData().getGame().getGameOwner();

        Player player = players.get(position);

        holder.playerNameView.setText(player.getName());

        if (player.equals(thisPlayer))
            holder.isCurrentPlayer.setText(R.string.playerList_IsYou);

        if (player.equals(gameOwner))
            holder.isGameOwner.setText(R.string.playerList_gameOwner);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {

        protected TextView playerNameView;
        protected TextView isGameOwner;
        protected TextView isCurrentPlayer;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameView = itemView.findViewById(R.id.player_name);
            isGameOwner = itemView.findViewById(R.id.isGameOwner);
            isCurrentPlayer = itemView.findViewById(R.id.isCurrentPlayer);
        }
    }
}
