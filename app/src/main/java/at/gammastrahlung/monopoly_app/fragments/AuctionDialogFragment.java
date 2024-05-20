package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.activities.BoardActivity;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class AuctionDialogFragment extends DialogFragment {

    private MonopolyClient monopolyClient;
    private EditText bidInput;
    private TextView resultTextView;
    private Button bidButton;

    public static AuctionDialogFragment newInstance(String propertyName, int propertyId) {
        AuctionDialogFragment fragment = new AuctionDialogFragment();
        Bundle args = new Bundle();
        args.putString("property_name", propertyName);
        args.putInt("property_id", propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_auction, null);

        TextView propertyNameView = view.findViewById(R.id.property_name);
        bidInput = view.findViewById(R.id.bid_input);
        bidButton = view.findViewById(R.id.bid_button);
        resultTextView = view.findViewById(R.id.result);
        Button backButton = view.findViewById(R.id.back_button);

        String propertyName = getArguments().getString("property_name");
        int propertyId = getArguments().getInt("property_id");

        propertyNameView.setText(propertyName);

        bidButton.setOnClickListener(v -> onBidButtonClicked(propertyId));
        backButton.setOnClickListener(v -> {
            dismiss();
            ((BoardActivity) getActivity()).returnToBoard();
        });

        builder.setView(view);
        return builder.create();
    }

    private void onBidButtonClicked(int propertyId) {
        try {
            int bid = Integer.parseInt(bidInput.getText().toString());
            Player currentPlayer = GameData.getGameData().getPlayer();

            if (currentPlayer.getBalance() >= bid) {
                monopolyClient.placeBid(bid); // Assume placeBid only needs bid amount
                currentPlayer.setBalance(currentPlayer.getBalance() - bid); // Deduct the bid amount from the player's balance
                bidInput.setVisibility(View.GONE);
                int remainingBalance = currentPlayer.getBalance(); // Calculate remaining balance
                resultTextView.setText(getString(R.string.result) + " " + remainingBalance); // Display remaining balance
                resultTextView.setVisibility(View.VISIBLE);
                bidButton.setEnabled(false); // Disable the submit button after successful bid
            } else {
                resultTextView.setText(R.string.error_insufficient_funds);
                resultTextView.setVisibility(View.VISIBLE);
            }
        } catch (NumberFormatException e) {
            resultTextView.setText(R.string.error_invalid_bid);
            resultTextView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            resultTextView.setText("An error occurred: " + e.getMessage());
            resultTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        monopolyClient = MonopolyClient.getMonopolyClient();
    }
}