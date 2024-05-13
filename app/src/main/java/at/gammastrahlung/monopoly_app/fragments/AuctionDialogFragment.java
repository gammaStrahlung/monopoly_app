package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
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
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class AuctionDialogFragment extends DialogFragment {

    private MonopolyClient monopolyClient;
    private EditText bidInput;
    private TextView resultTextView;

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
        Button bidButton = view.findViewById(R.id.bid_button);
        resultTextView = view.findViewById(R.id.result);
        Button backButton = view.findViewById(R.id.back_button);

        propertyNameView.setText(getArguments().getString("property_name"));

        bidButton.setOnClickListener(v -> onBidButtonClicked());
        backButton.setOnClickListener(v -> dismiss());

        builder.setView(view)
                .setNegativeButton("Cancel", (dialog, id) -> dismiss())
                .setPositiveButton("End Auction", (dialog, id) -> finalizeAuction());

        return builder.create();
    }

    private void onBidButtonClicked() {
        try {
            int bid = Integer.parseInt(bidInput.getText().toString());
            monopolyClient.placeBid(bid); // Assume placeBid only needs bid amount
            resultTextView.setText(getString(R.string.enter_bid , bid));
        } catch (NumberFormatException e) {
            resultTextView.setText(R.string.error_invalid_bid);
        }
    }

    private void finalizeAuction() {
        // Logic to finalize the auction
        monopolyClient.finalizeAuction();
        dismiss();
    }
 }