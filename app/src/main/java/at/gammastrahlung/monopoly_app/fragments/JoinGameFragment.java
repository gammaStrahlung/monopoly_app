package at.gammastrahlung.monopoly_app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.activities.BoardActivity;
import at.gammastrahlung.monopoly_app.activities.LobbyActivity;
import at.gammastrahlung.monopoly_app.game.Game;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.helpers.DialogDataValidation;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class JoinGameFragment extends DialogFragment {

    EditText playerNameEditText;
    EditText gameIdEditText;

    @NonNull
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

                    Activity activity = getActivity();

                    GameData.getGameData().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable sender, int propertyId) {
                            if (propertyId == BR.game) {

                                // Game was changed
                                if (GameData.getGameData().getGame() == null) {
                                    // Error popup
                                    activity.runOnUiThread(() -> Toast.makeText(activity, R.string.joinGame_fail, Toast.LENGTH_LONG).show());
                                } else {
                                    // Joined game

                                    // Save gameId (used for re-joining)
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
                                    SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                                    preferenceEditor.putInt("gameId", GameData.getGameData().getGame().getGameId());
                                    preferenceEditor.apply();

                                    Intent intent = GameData.getGameData().getGame().getState() == Game.GameState.PLAYING ?
                                            new Intent(activity, BoardActivity.class) : // Playing -> game board
                                            new Intent(activity, LobbyActivity.class); // Not already playing -> game lobby

                                    activity.startActivity(intent);
                                }

                                GameData.getGameData().removeOnPropertyChangedCallback(this);
                            }
                        }
                    });
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
