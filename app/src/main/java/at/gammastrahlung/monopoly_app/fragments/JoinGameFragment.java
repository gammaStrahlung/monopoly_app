package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.helpers.DialogDataValidation;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class JoinGameFragment extends DialogFragment {

    EditText playerNameEditText;
    EditText gameIdEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflated = inflater.inflate(R.layout.dialog_joingame, null);

        // Set EditText views
        playerNameEditText = inflated.findViewById(R.id.playerName);
        gameIdEditText = inflated.findViewById(R.id.gameId);

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.main_joinGame, (dialog, id) -> {

                    String playerName = playerNameEditText.getText().toString();
                    int gameId = Integer.parseInt(gameIdEditText.getText().toString());

                    MonopolyClient.getMonopolyClient().joinGame(gameId, playerName);
                })
                .setNegativeButton(R.string.dialog_join_cancel, (dialog, id) -> {
                    dialog.cancel(); // Cancel the dialog
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog == null)
            return; // Prevents NullPointerException

        // Validation has to be added in onResume, because buttons are null in onCreateDialog
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        // Disable positive button until input is valid
        positiveButton.setEnabled(false);

        // Add TextChangeListener to EditTexts
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveButton.setEnabled(
                        DialogDataValidation.validateGameId(gameIdEditText.getText().toString()) &&
                        DialogDataValidation.validatePlayerName(playerNameEditText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        gameIdEditText.addTextChangedListener(textWatcher);
        playerNameEditText.addTextChangedListener(textWatcher);
    }
}
