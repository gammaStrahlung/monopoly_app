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

public class JoinGameFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflated = inflater.inflate(R.layout.dialog_joingame, null);

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.main_joinGame, (dialog, id) -> {
                    EditText playerNameET = inflated.findViewById(R.id.playerName);
                    EditText gameIdET = inflated.findViewById(R.id.gameId);

                    String playerName = playerNameET.getText().toString();
                    int gameId = 0;
                    try {
                        gameId = Integer.parseInt(gameIdET.getText().toString());
                    } catch (NumberFormatException e) {
                        return; // Not a number
                    }

                    if (playerName.isEmpty())
                        return; // Handle empty name here

                    MonopolyClient.getMonopolyClient().joinGame(gameId, playerName);
                })
                .setNegativeButton(R.string.dialog_join_cancel, (dialog, id) -> {
                    dialog.cancel(); // Cancel the dialog
                });

        return builder.create();
    }
}
