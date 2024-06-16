package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.activities.BoardActivity;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class PurchaseDialogFragment extends DialogFragment {

    private PurchaseDialogListener listener;

    public static PurchaseDialogFragment newInstance() {
      return new PurchaseDialogFragment();
    }

    public void setPurchaseDialogListener(PurchaseDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_property_purchase_dialog, null);

        TextView promptText = view.findViewById(R.id.purchase_prompt_text);
        Button yesButton = view.findViewById(R.id.yes_button);
        Button noButton = view.findViewById(R.id.no_button);


        promptText.setText("Do you want to buy the Property ?");

        yesButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onYesButtonClicked();
            }

            MonopolyClient.getMonopolyClient().buyPropertythroughPurchaseDialog(BoardActivity.getFollowingIndex());
            dismiss();
        });

 noButton.setOnClickListener(v -> {

  MonopolyClient.getMonopolyClient().sendstartAuctionMessage();


    dismiss();
});

        builder.setView(view);
        return builder.create();
    }

    public interface PurchaseDialogListener {
        void onNoButtonClicked();

        void onYesButtonClicked();
    }


}