package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;

public class JoinGameFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_joingame, null))

                // Add action buttons
                .setPositiveButton(R.string.main_joinGame, (dialog, id) -> {
                    // TODO Connect
                })
                .setNegativeButton(R.string.dialog_join_cancel, (dialog, id) -> {
                    dialog.cancel(); // Cancel the dialog
                });

        return builder.create();
    }
}
