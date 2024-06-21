package at.gammastrahlung.monopoly_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.fragments.PlayerInfoFragment;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    ObservableArrayList<Player> players;
    AppCompatActivity context;
    private final boolean showMoney;
    private final boolean enablePlayerInfo;
    private final boolean showOwner;
    private boolean emphasizeCurrentPlayer;

    public PlayerAdapter(ObservableArrayList<Player> players, Context context, boolean showMoney, boolean enablePlayerInfo, boolean showOwner, boolean emphasizeCurrentPlayer) {
        this.players = players;
        this.context = (AppCompatActivity) context;
        this.showMoney = showMoney;
        this.enablePlayerInfo = enablePlayerInfo;
        this.showOwner = showOwner;
        this.emphasizeCurrentPlayer = emphasizeCurrentPlayer;
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
        Player currentPlayer = GameData.getGameData().getCurrentPlayer();
        Player player = players.get(position);

        holder.playerNameView.setText(player.getName());
        int playerIconResourceId = context.getResources().getIdentifier("playericon_" + (position + 1), "drawable", context.getPackageName());
        holder.playerIcon.setImageResource(playerIconResourceId);

        if (player.equals(thisPlayer))
            holder.isCurrentPlayer.setText(R.string.playerList_IsYou);

        if (showOwner) {
            if (player.equals(gameOwner))
                holder.isGameOwner.setText(R.string.playerList_gameOwner);
        }

        if (emphasizeCurrentPlayer) {
            if (player.equals(currentPlayer)) {
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        if (showMoney)
            holder.playerMoney.setText(context.getString(R.string.currencyDisplay, player.getBalance()));

        if (enablePlayerInfo)
            holder.itemView.setOnClickListener(v -> new PlayerInfoFragment(player).show(context.getSupportFragmentManager(), "PlayerInfo"));
    }

    public void updatePlayers(List<Player> updatedPlayers) {
        players = new ObservableArrayList<>();
        players.addAll(updatedPlayers);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {

        protected TextView playerNameView;
        protected TextView isGameOwner;
        protected TextView isCurrentPlayer;
        protected TextView playerMoney;

        protected ImageView playerIcon;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameView = itemView.findViewById(R.id.player_name);
            isGameOwner = itemView.findViewById(R.id.isGameOwner);
            isCurrentPlayer = itemView.findViewById(R.id.isCurrentPlayer);
            playerMoney = itemView.findViewById(R.id.playerMoney);
            playerIcon = itemView.findViewById(R.id.player_icon);
        }
    }
}
