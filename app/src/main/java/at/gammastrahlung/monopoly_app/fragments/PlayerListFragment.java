package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.adapters.PlayerAdapter;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;

public class PlayerListFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView = inflater.inflate(R.layout.fragment_playerlist, null);

        // Set RecyclerView adapter
        RecyclerView playersList = inflatedView.findViewById(R.id.playersList);
        playersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();
        RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter = new PlayerAdapter(players, getActivity(), true, true, true);
        playersList.setAdapter(adapter);

        // Add close button
        builder.setView(inflatedView).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));

        return builder.create();
    }
}
