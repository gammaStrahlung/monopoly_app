package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

public class UncoverPlayerListFragment extends DialogFragment {

    private TextView uncoverCheaterTextview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView = inflater.inflate(R.layout.fragment_playerlist_cheater, null);

        uncoverCheaterTextview = inflatedView.findViewById(R.id.selectCheaterTextview);

        RecyclerView playersList = inflatedView.findViewById(R.id.cheaterPlayerList);
        playersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();
        RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter = new PlayerAdapter(players, getActivity(), false, false);
        playersList.setAdapter(adapter);

        builder.setView(inflatedView).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));

        return builder.create();
    }
}
