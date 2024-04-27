package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textview.MaterialTextView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;

public class FieldInfoFragment extends DialogFragment {
    private final Field field;

    public FieldInfoFragment(Field field) {
        this.field = field;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView = inflater.inflate(R.layout.fragment_fieldinfo, null);

        // Add close button
        builder.setView(inflatedView).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));

        // Add actual field display code here
        MaterialTextView title = inflatedView.findViewById(R.id.fieldName);
        title.setText(field.getName());

        return builder.create();
    }
}
