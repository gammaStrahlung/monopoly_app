package at.gammastrahlung.monopoly_app.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.game.Player;

public class PlayerListChangedCallback extends ObservableList.OnListChangedCallback<ObservableList<Player>> {

    private final Activity activity;
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter;

    public PlayerListChangedCallback(Activity activity, RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onChanged(ObservableList sender) {
        activity.runOnUiThread(() -> adapter.notifyDataSetChanged());
    }

    @Override
    public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
        activity.runOnUiThread(() -> adapter.notifyItemRangeChanged(positionStart, positionStart));
    }

    @Override
    public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
        activity.runOnUiThread(() -> adapter.notifyItemRangeInserted(positionStart, itemCount));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
        // ItemRangeMoved is not available in RecyclerView.Adapter
        activity.runOnUiThread(() -> adapter.notifyDataSetChanged());
    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
        activity.runOnUiThread(() -> adapter.notifyItemRangeRemoved(positionStart, itemCount));
    }
}
