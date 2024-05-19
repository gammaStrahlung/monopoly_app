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

public class PurchaseDialogFragment extends DialogFragment {

    public interface PurchaseDialogListener {
        void onNoButtonClicked();
        void onYesButtonClicked();
    }

    private PurchaseDialogListener listener;

    public static PurchaseDialogFragment newInstance(String propertyName, int propertyId) {
        PurchaseDialogFragment fragment = new PurchaseDialogFragment();
        Bundle args = new Bundle();
        args.putString("property_name", propertyName);
        args.putInt("property_id", propertyId);
        fragment.setArguments(args);
        return fragment;
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

        String propertyName = getArguments().getString("property_name");
        int propertyId = getArguments().getInt("property_id");

        promptText.setText("Möchten Sie " + propertyName + " kaufen?");

        yesButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onYesButtonClicked();
            }
            ((BoardActivity) getActivity()).handlePropertyPurchaseDecision(true);
            dismiss();
        });

        noButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNoButtonClicked();
            }
            ((BoardActivity) getActivity()).handlePropertyPurchaseDecision(false);
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}

