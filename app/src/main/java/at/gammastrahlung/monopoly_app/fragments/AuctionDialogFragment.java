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

























}