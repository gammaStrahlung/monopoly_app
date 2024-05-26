package at.gammastrahlung.monopoly_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;

public class MortgageManagementFragment extends Fragment {
    private TextView titleTextView;
    private TextView currentBalanceTextView;
    private TextView saleValueTextView;
    private Button mortgageButton;
    private Button unmortgageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mortgage, container, false);

        titleTextView = view.findViewById(R.id.title);
        currentBalanceTextView = view.findViewById(R.id.current_balance);
        saleValueTextView = view.findViewById(R.id.sale_value_display);
        mortgageButton = view.findViewById(R.id.mortgage_button);
        unmortgageButton = view.findViewById(R.id.unmortgage_button);

        mortgageButton.setOnClickListener(v -> handleMortgage());
        unmortgageButton.setOnClickListener(v -> handleUnmortgage());

        updateBalanceDisplay();

        return view;
    }

    private void handleMortgage() {
        // Logic to mortgage a property
        Player currentPlayer = GameData.getGameData().getPlayer();
        currentPlayer.setBalance(currentPlayer.getBalance() - 100); // Dummy value for mortgage
        updateBalanceDisplay();
        showTemporarySaleValue(true, 100); // Dummy mortgage value
    }

    private void handleUnmortgage() {
        // Logic to unmortgage a property
        Player currentPlayer = GameData.getGameData().getPlayer();
        currentPlayer.setBalance(currentPlayer.getBalance() + 110); // Dummy value for unmortgage
        updateBalanceDisplay();
        showTemporarySaleValue(false, 110); // Dummy unmortgage value
    }

    private void updateBalanceDisplay() {
        Player currentPlayer = GameData.getGameData().getPlayer();
        currentBalanceTextView.setText("Current Balance: $" + currentPlayer.getBalance());
    }

    private void showTemporarySaleValue(boolean isMortgage, int amount) {
        saleValueTextView.setVisibility(View.VISIBLE);
        if (isMortgage) {
            saleValueTextView.setText("Mortgaged for: $" + amount);
        } else {
            saleValueTextView.setText("Unmortgaged for: $" + amount);
        }

        // This TextView could be hidden after a delay or based on user interaction
        saleValueTextView.postDelayed(() -> saleValueTextView.setVisibility(View.GONE), 2000);
    }
}