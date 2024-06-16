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
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class UncoverPlayerListFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView = inflater.inflate(R.layout.fragment_playerlist_cheater, null);

        RecyclerView playersList = inflatedView.findViewById(R.id.cheaterPlayerList);
        playersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();

        RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter = new PlayerAdapter(players, getActivity(), false, false, false, false);
        playersList.setAdapter(adapter);

        playersList.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                view.setOnClickListener(v -> {
                    int position = playersList.getChildAdapterPosition(view);
                    Player player = players.get(position);
                    if (player.equals(GameData.getGameData().getPlayer())) {
                        showSelfSelectDialog();
                    } else {
                        showCheaterDialog(player);
                    }
                });
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                view.setOnClickListener(null);
            }
        });

        builder.setView(inflatedView).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));

        return builder.create();
    }

    private void showCheaterDialog(Player player) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure player " + player.getName() + " is a cheater?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    if (player.isCheating()) {
                        int index = GameData.getGameData().getPlayers().indexOf(player);
                        MonopolyClient.getMonopolyClient().reportCheat(index);
                        MonopolyClient.getMonopolyClient().reportAward();

                        new AlertDialog.Builder(getActivity())
                                .setMessage("This player was a cheater, you collect 200!")
                                .setPositiveButton("Ok", (dialog2, which2) -> dialog2.dismiss())
                                .create()
                                .show();

                    } else {
                        MonopolyClient.getMonopolyClient().reportPenalty();
                        new AlertDialog.Builder(getActivity())
                                .setMessage("This player was not a cheater, you loose 200!")
                                .setPositiveButton("Ok", (dialog3, which3) -> dialog3.dismiss())
                                .create()
                                .show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showSelfSelectDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("You are not allowed to select yourself")
                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
