package at.gammastrahlung.monopoly_app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class ReJoinGameFragment extends DialogFragment {

    private final int gameId;
    public ReJoinGameFragment(int gameId) {
        this.gameId = gameId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflated = inflater.inflate(R.layout.dialog_rejoin, null);

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.yes, (dialog, id) -> {

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
                                    // Re-joined game
                                    Intent intent = GameData.getGameData().getGame().getState() == Game.GameState.PLAYING ?
                                            new Intent(activity, BoardActivity.class) : // Playing -> game board
                                            new Intent(activity, LobbyActivity.class); // Not already playing -> game lobby

                                    // Clear previous activities
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    // Update GameData.currentPlayer
                                    MonopolyClient.getMonopolyClient().initiateRound();

                                    activity.startActivity(intent);
                                }

                                GameData.getGameData().removeOnPropertyChangedCallback(this);
                            }
                        }
                    });

                    // Player name can be empty when re-joining as the old player name is saved on
                    // the server
                    MonopolyClient.getMonopolyClient().joinGame(gameId, "");
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {});

        return builder.create();
    }
}
