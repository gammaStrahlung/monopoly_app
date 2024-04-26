package at.gammastrahlung.monopoly_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textview.MaterialTextView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;

public class FieldInfoFragment extends DialogFragment {
    private View inflatedView;
    private final Field field;

    public FieldInfoFragment(Field field) {
        this.field = field;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_fieldinfo, container, false);
        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add actual field display code here
        MaterialTextView title = inflatedView.findViewById(R.id.fieldName);
        title.setText(field.getName());
    }
}
