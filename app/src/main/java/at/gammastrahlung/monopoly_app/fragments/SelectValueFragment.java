package at.gammastrahlung.monopoly_app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;

public class SelectValueFragment extends DialogFragment {

    public interface OnValueSelectedListener {
        void onValueSelected(int value);
    }

    private OnValueSelectedListener listener;
    private EditText diceValueEditText;
    private Button buttonForward;
    private Button buttonSelect;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView = inflater.inflate(R.layout.fragment_valueselection, null);
        builder.setView(inflatedView);

        diceValueEditText = inflatedView.findViewById(R.id.diceValueEditText);
        buttonForward = inflatedView.findViewById(R.id.buttonForward);
        buttonSelect = inflatedView.findViewById(R.id.buttonSelect);

        int value1 = GameData.getGameData().getDice().getValue1();
        int value2 = GameData.getGameData().getDice().getValue2();

        int dicedValue = value1 + value2;
        diceValueEditText.setText(String.valueOf(dicedValue));

        buttonForward.setOnClickListener(v -> forwardButtonClick());
        buttonSelect.setOnClickListener(v -> selectValueClick());

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnValueSelectedListener) context;
    }

    private void forwardButtonClick() {
        int selectedValue = Integer.parseInt(diceValueEditText.getText().toString());
        listener.onValueSelected(selectedValue);
        dismiss();
    }

    private void selectValueClick() {
        if (buttonSelect.getText().equals("Cheat")) {
            diceValueEditText.setEnabled(true);
            diceValueEditText.setTextColor(getResources().getColor(android.R.color.black));
            diceValueEditText.requestFocus();

            buttonForward.setVisibility(View.GONE);
            buttonSelect.setText("Submit");

        } else {
            int selectedValue = Integer.parseInt(diceValueEditText.getText().toString());
            listener.onValueSelected(selectedValue);
            dismiss();
        }
    }
}

