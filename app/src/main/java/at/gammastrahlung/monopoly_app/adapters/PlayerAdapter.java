package at.gammastrahlung.monopoly_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.fragments.PlayerInfoFragment;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    ObservableArrayList<Player> players;
    AppCompatActivity context;
    private final boolean showMoney;
    private final boolean enablePlayerInfo;

    public PlayerAdapter(ObservableArrayList<Player> players, Context context, boolean showMoney, boolean enablePlayerInfo) {
        this.players = players;
        this.context = (AppCompatActivity) context;
        this.showMoney = showMoney;
        this.enablePlayerInfo = enablePlayerInfo;
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

        if (showMoney)
            holder.playerMoney.setText(context.getString(R.string.currencyDisplay, thisPlayer.getBalance()));

        if (enablePlayerInfo)
            holder.itemView.setOnClickListener(v -> new PlayerInfoFragment(player).show(context.getSupportFragmentManager(), "PlayerInfo"));
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

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameView = itemView.findViewById(R.id.player_name);
            isGameOwner = itemView.findViewById(R.id.isGameOwner);
            isCurrentPlayer = itemView.findViewById(R.id.isCurrentPlayer);
            playerMoney = itemView.findViewById(R.id.playerMoney);
        }
    }
}
