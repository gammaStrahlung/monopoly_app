package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class NewGameFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflated = inflater.inflate(R.layout.dialog_newgame, null);

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.main_startGame, (dialog, id) -> {
                    EditText text = inflated.findViewById(R.id.playerName);
                    String playerName = text.getText().toString();

                    if (playerName.isEmpty())
                        return; // Handle empty input here

                    MonopolyClient.getMonopolyClient().newGame(playerName);
                })
                .setNegativeButton(R.string.dialog_join_cancel, (dialog, id) -> {
                    dialog.cancel(); // Cancel the dialog
                });

        return builder.create();
    }
}
