package at.gammastrahlung.monopoly_app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_app.game.gameboard.Utility;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;

public class BuyFieldFragment extends DialogFragment {

    private final Field field;

    public BuyFieldFragment(Field field) {
        this.field = field;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflated = inflater.inflate(R.layout.dialog_buyfield, null);

        int fieldPrice = 0;

        if (field instanceof Property)
            fieldPrice = ((Property) field).getPrice();
        else if (field instanceof Utility)
            fieldPrice = ((Utility) field).getPrice();
        else if (field instanceof Railroad)
            fieldPrice = ((Railroad) field).getPrice();

        TextView buyText = inflated.findViewById(R.id.buy_title);
        buyText.setText(getString(R.string.buyField, field.getName(), String.valueOf(fieldPrice)));

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.yes, (dialog, id) -> {

                    Activity activity = getActivity();

                    GameData.getGameData().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable sender, int propertyId) {
                            if (propertyId == BR.game) {
                                Player thisPlayer = GameData.getGameData().getPlayer();
                                Field newField = GameData.getGameData().getGame().getGameBoard().getFields()[field.getFieldId()];

                                if (newField instanceof Property && ((Property) newField).getOwner().equals(thisPlayer) ||
                                        newField instanceof Utility && ((Utility) newField).getOwner().equals(thisPlayer) ||
                                        newField instanceof Railroad && ((Railroad) newField).getOwner().equals(thisPlayer)) {
                                    // Field has been bought successfully

                                    GameData.getGameData().removeOnPropertyChangedCallback(this);
                                    activity.runOnUiThread(() -> Toast.makeText(activity, R.string.buy_successful, Toast.LENGTH_LONG).show());
                                } else if (GameData.getGameData().getLastMessageType() == ServerMessage.MessageType.ERROR) {
                                    // Failed to buy field
                                    GameData.getGameData().removeOnPropertyChangedCallback(this);
                                    activity.runOnUiThread(() -> Toast.makeText(activity, R.string.buy_fail, Toast.LENGTH_LONG).show());
                                }
                            }
                        }
                    });

                    MonopolyClient.getMonopolyClient().buyField(field.getFieldId());
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {
                });

        return builder.create();
    }
}
