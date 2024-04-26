package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textview.MaterialTextView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.Player;

public class PlayerInfoFragment extends DialogFragment {
    private final Player player;

    public PlayerInfoFragment(Player player) {
        this.player = player;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView = inflater.inflate(R.layout.fragment_playerinfo, null);

        // Add close button
        builder.setView(inflatedView).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));

        // Add actual player display code here (owned fields, balance, ...)
        MaterialTextView title = inflatedView.findViewById(R.id.player_name);
        title.setText(player.getName());

        return builder.create();
    }
}
