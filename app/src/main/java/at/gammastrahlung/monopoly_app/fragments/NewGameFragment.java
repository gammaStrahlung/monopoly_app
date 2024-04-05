package at.gammastrahlung.monopoly_app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.activities.LobbyActivity;
import at.gammastrahlung.monopoly_app.game.GameData;
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

                    Activity activity = getActivity();

                    GameData.getGameData().getGameId().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable sender, int propertyId) {
                            // Received data from the server
                            ObservableInt gameId = (ObservableInt) sender;
                            if (gameId.get() == -1) {
                                // Error popup
                                Toast.makeText(activity, R.string.newGame_fail, Toast.LENGTH_LONG).show();
                                gameId.set(0);
                            } else {
                                // Created game -> Start lobby activity
                                Intent intent = new Intent(activity, LobbyActivity.class);
                                activity.startActivity(intent);
                            }
                        }
                    });
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
