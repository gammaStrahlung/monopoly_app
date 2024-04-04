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

public class NewGameFragment extends DialogFragment {

    private EditText playerNameEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflated = inflater.inflate(R.layout.dialog_newgame, null);

        playerNameEditText = inflated.findViewById(R.id.playerName);

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.main_startGame, (dialog, id) -> {
                    String playerName = playerNameEditText.getText().toString();
                    MonopolyClient.getMonopolyClient().newGame(playerName);
                })
                .setNegativeButton(R.string.dialog_join_cancel, (dialog, id) -> {});

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
                positiveButton.setEnabled(DialogDataValidation.validatePlayerName(playerNameEditText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        playerNameEditText.addTextChangedListener(textWatcher);
    }
}
