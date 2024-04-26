package at.gammastrahlung.monopoly_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.adapters.PlayerAdapter;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;

public class PlayerListFragment extends DialogFragment {
    private View inflatedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_playerlist, container, false);
        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set RecyclerView adapter
        RecyclerView playersList = inflatedView.findViewById(R.id.playersList);
        playersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();
        RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter = new PlayerAdapter(players, getActivity(), true, true);
        playersList.setAdapter(adapter);
    }
}
