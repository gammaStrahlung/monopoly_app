package at.gammastrahlung.monopoly_app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class SelectValueFragment extends DialogFragment {

    public interface OnValueSelectedListener {
        void onForward(int value);
        void onSelectedValue(int value);
    }

    private OnValueSelectedListener listener;
    private EditText diceValueEditText;
    private TextView enterValueTextView;
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
        enterValueTextView = inflatedView.findViewById(R.id.textView);
        buttonForward = inflatedView.findViewById(R.id.buttonForward);
        buttonSelect = inflatedView.findViewById(R.id.buttonSelect);

        int value1 = GameData.getGameData().getDice().getValue1();
        int value2 = GameData.getGameData().getDice().getValue2();

        int dicedValue = value1 + value2;
        diceValueEditText.setText(String.valueOf(dicedValue));

        buttonForward.setOnClickListener(v -> forwardButtonClick());
        buttonSelect.setOnClickListener(v -> selectValueClick());

        diceValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonSelect.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Dialog dialog = builder.create();
        Window window = dialog.getWindow();

        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();

            WindowManager windowManager = (WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenHeight = size.y;

            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            params.y = screenHeight / 2;
            window.setAttributes(params);
        }

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnValueSelectedListener) context;
    }

    private void forwardButtonClick() {
        int selectedValue = Integer.parseInt(diceValueEditText.getText().toString());
        listener.onForward(selectedValue);
        dismiss();
    }

    private void selectValueClick() {
        if (buttonSelect.getText().equals("Cheat")) {
            enterValueTextView.setText(R.string.enter_the_value_to_forward);
            diceValueEditText.setEnabled(true);
            diceValueEditText.setText("");
            diceValueEditText.setTextColor(getResources().getColor(android.R.color.black));
            diceValueEditText.requestFocus();

            buttonForward.setVisibility(View.GONE);
            buttonSelect.setText(R.string.submit);

        } else {
            int selectedValue = Integer.parseInt(diceValueEditText.getText().toString());
            int dicedValue = GameData.getGameData().getDice().getValue1() + GameData.getGameData().getDice().getValue2();

            if (selectedValue == dicedValue) {
                Toast.makeText(getContext(), R.string.selected_value_matches_diced_value, Toast.LENGTH_SHORT).show();
                listener.onForward(selectedValue);
                dismiss();

            } else {

                if (selectedValue <= 1) {
                    Toast.makeText(getContext(), R.string.the_selected_value_needs_to_be_greater_than_1, Toast.LENGTH_SHORT).show();
                    diceValueEditText.setText("");
                    diceValueEditText.requestFocus();
                    return;
                }
                int value1 = selectedValue / 2;
                int value2 = selectedValue - value1;

                MonopolyClient.getMonopolyClient().cheating(value1, value2);

                listener.onSelectedValue(selectedValue);
                dismiss();
            }
        }
    }
}

