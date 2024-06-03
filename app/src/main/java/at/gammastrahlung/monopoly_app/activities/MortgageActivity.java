package at.gammastrahlung.monopoly_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.FieldType;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.PropertyColor;
import at.gammastrahlung.monopoly_app.helpers.MortgageHelper;

import java.util.ArrayList;
import java.util.List;

public class MortgageActivity extends AppCompatActivity {

    private Button mortgageButton;
    private Button unmortgageButton;
    private Property selectedProperty;
    private List<Property> properties;
    private PropertyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mortgage);

        mortgageButton = findViewById(R.id.mortgage_button);
        unmortgageButton = findViewById(R.id.unmortgage_button);
        RecyclerView propertyListView = findViewById(R.id.property_list_view);
        properties = new ArrayList<>();

        Player currentPlayer = GameData.getGameData().getPlayer();
        properties.addAll(getPropertiesOwnedByPlayer(currentPlayer));
        adapter = new PropertyAdapter(properties);
        propertyListView.setAdapter(adapter);

        mortgageButton.setOnClickListener(v -> {
            if (selectedProperty != null) {
                String result = MortgageHelper.manageMortgage(selectedProperty, true);
                updateUI(result);
                refreshPropertyList();
            }
        });

        unmortgageButton.setOnClickListener(v -> {
            if (selectedProperty != null) {
                String result = MortgageHelper.manageMortgage(selectedProperty, false);
                updateUI(result);
                refreshPropertyList();
            }
        });

        updateButtons();
        refreshPropertyList();
    }

    private void updateUI(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        updateBalanceDisplay();
    }

    private void updateBalanceDisplay() {
        Player currentPlayer = GameData.getGameData().getPlayer();
        TextView currentBalanceTextView = findViewById(R.id.current_balance);
        currentBalanceTextView.setText("Current Balance: $" + currentPlayer.getBalance());
    }

    private void refreshPropertyList() {
        Player currentPlayer = GameData.getGameData().getPlayer();
        properties.clear();
        properties.addAll(getPropertiesOwnedByPlayer(currentPlayer));
        adapter.notifyDataSetChanged();
    }

    private List<Property> getPropertiesOwnedByPlayer(Player player) {
        List<Property> ownedProperties = new ArrayList<>();
        Field[] fields = GameData.getGameData().getGame().getGameBoard().getFields();

        for (Field field : fields) {
            if (field instanceof Property) {
                Property property = (Property) field;
                if (property.getOwner() != null && property.getOwner().equals(player)) {
                    ownedProperties.add(property);
                }
            }
        }

        return ownedProperties;
    }

    private class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {
        private List<Property> properties;

        PropertyAdapter(List<Property> properties) {
            this.properties = properties;
        }

        @NonNull
        @Override
        public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mortgage, parent, false);
            return new PropertyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
            Property property = properties.get(position);
            holder.bind(property);
            holder.itemView.setOnClickListener(v -> {
                selectedProperty = property;
                notifyDataSetChanged();
                updateButtons();
            });

            if (property.equals(selectedProperty)) {
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public int getItemCount() {
            return properties.size();
        }
    }

    private class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView saleValueTextView;

        PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            saleValueTextView = itemView.findViewById(R.id.sale_value_display);
        }

        void bind(Property property) {
            titleTextView.setText(property.getName());
            saleValueTextView.setText("Sale Value: $" + property.getMortgageValue());
        }
    }

    private void updateButtons() {
        if (selectedProperty == null) {
            mortgageButton.setEnabled(false);
            unmortgageButton.setEnabled(false);
        } else {
            mortgageButton.setEnabled(!selectedProperty.isMortgaged());
            unmortgageButton.setEnabled(selectedProperty.isMortgaged());
        }
    }
}
