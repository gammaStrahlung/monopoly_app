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
import at.gammastrahlung.monopoly_app.game.gameboard.Property;

public class MortgageManagementFragment extends Fragment {
    private TextView titleTextView;
    private TextView currentBalanceTextView;
    private TextView saleValueTextView;
    private Button mortgageButton;
    private Button unmortgageButton;
    private Property selectedProperty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mortgage, container, false);

        titleTextView = view.findViewById(R.id.title);
        currentBalanceTextView = view.findViewById(R.id.current_balance);
        saleValueTextView = view.findViewById(R.id.sale_value_display);
        mortgageButton = view.findViewById(R.id.mortgage_button);
        unmortgageButton = view.findViewById(R.id.unmortgage_button);

        Player currentPlayer = GameData.getGameData().getPlayer();
        mortgageButton.setOnClickListener(v -> {
            if (selectedProperty != null) {
                mortgageProperty(selectedProperty);
            }
        });
        unmortgageButton.setOnClickListener(v -> {
            if (selectedProperty != null) {
                unmortgageProperty(selectedProperty);
            }
        });

        updateBalanceDisplay();
        return view;
    }

    private void mortgageProperty(Property property) {
        if (!property.isMortgaged()) {
            Player currentPlayer = GameData.getGameData().getPlayer();
            currentPlayer.setBalance(currentPlayer.getBalance() + property.getMortgageValue());
            property.setMortgaged(true);
            updateBalanceDisplay();
            showTemporarySaleValue(true, property.getMortgageValue());
        }
    }

    private void unmortgageProperty(Property property) {
        if (property.isMortgaged()) {
            Player currentPlayer = GameData.getGameData().getPlayer();
            int unmortgageCost = (int) (property.getMortgageValue() * 1.1); // 10% fee
            currentPlayer.setBalance(currentPlayer.getBalance() - unmortgageCost);
            property.setMortgaged(false);
            updateBalanceDisplay();
            showTemporarySaleValue(false, unmortgageCost);
        }
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
        saleValueTextView.postDelayed(() -> saleValueTextView.setVisibility(View.GONE), 2000);
    }
}
