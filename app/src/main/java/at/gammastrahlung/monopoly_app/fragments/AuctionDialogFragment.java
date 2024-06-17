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
import at.gammastrahlung.monopoly_app.activities.BoardActivity;
import at.gammastrahlung.monopoly_app.game.Bid;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;
import at.gammastrahlung.monopoly_app.network.WebSocketHandler;

public class AuctionDialogFragment extends DialogFragment implements WebSocketHandler.ResultBidTrigger {
    private EditText bidInput;
    private TextView resultTextView;

    private Button bidButton;


    public static AuctionDialogFragment newInstance() {
        return new AuctionDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Button noButton;
        WebSocketHandler webSocketHandlerforResultBidTrigger;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_auction, null);
        webSocketHandlerforResultBidTrigger = new WebSocketHandler();
        webSocketHandlerforResultBidTrigger.setResultBidTrigger(this);

        bidInput = view.findViewById(R.id.bid_input);
        resultTextView = view.findViewById(R.id.result);
        bidButton = view.findViewById(R.id.bid_button);
        noButton = view.findViewById(R.id.back_button);


        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBid();  // Call submitBid when the bid button is clicked
            }
        });


        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });





        builder.setView(view);
        return builder.create();
    }



    private void submitBid() {
        try {
            int bid = Integer.parseInt(bidInput.getText().toString());
            // Validates that the bid is a positive integer
            if (bid >= 0) {
                // Create an instance of the Bid class
                Bid bidInstance = new Bid();
                bidInstance.setPlayerForBid(GameData.getGameData().getCurrentPlayer());
                bidInstance.setAmount(bid);

                bidInstance.setFieldIndex(BoardActivity.getFollowingIndex());

                // Send the bid using the sendBid method
                MonopolyClient.getMonopolyClient().sendBid(bidInstance);


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

    @Override
    public void showResultBid(String resultBid) {
        resultTextView.setText(resultBid);
        resultTextView.setVisibility(View.VISIBLE);

    }
}