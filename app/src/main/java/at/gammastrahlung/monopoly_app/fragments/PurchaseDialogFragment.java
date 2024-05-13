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

    public static PurchaseDialogFragment newInstance(String propertyName) {
        PurchaseDialogFragment fragment = new PurchaseDialogFragment();
        Bundle args = new Bundle();
        args.putString("property_name", propertyName);
        fragment.setArguments(args);
        return fragment;
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
        promptText.setText("Möchten Sie " + propertyName + " kaufen?");

        yesButton.setOnClickListener(v -> {
            ((BoardActivity) getActivity()).handlePropertyPurchaseDecision(true);
            dismiss();
        });

        noButton.setOnClickListener(v -> {
            ((BoardActivity) getActivity()).handlePropertyPurchaseDecision(false);
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}
