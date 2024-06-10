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

public class AuctionDialogFragment extends DialogFragment {
    private EditText bidInput;
    private TextView resultTextView;
    private Button bidButton;
public static AuctionDialogFragment newInstance() {
    return new AuctionDialogFragment();
}

@NonNull
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.fragment_auction, null);

    // Initializes UI components from the layout
    bidInput = view.findViewById(R.id.bid_input);
    resultTextView = view.findViewById(R.id.result);
    bidButton = view.findViewById(R.id.bid_button);

    // Sets up the action for the bid button click
    bidButton.setOnClickListener(v -> submitBid());

    // Sets up the action for the back button to dismiss the dialog
    Button backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> dismiss());

    builder.setView(view);
    return builder.create();
}

private void submitBid() {
    try {
        int bid = Integer.parseInt(bidInput.getText().toString());
        // Validates that the bid is a positive integer
        if (bid > 0) {
            // TODO: Implement bid storage logic. Store the bid value along with the player's identifier.
            resultTextView.setText(getString(R.string.bid_submitted));
            resultTextView.setVisibility(View.VISIBLE);
            bidButton.setEnabled(false); // Disable the bid button after successful submission
        } else {
            resultTextView.setText(R.string.error_positive_number_required);
            resultTextView.setVisibility(View.VISIBLE);
        }
    } catch (NumberFormatException e) {
        resultTextView.setText(R.string.error_invalid_bid);
        resultTextView.setVisibility(View.VISIBLE);
    }
}

























}