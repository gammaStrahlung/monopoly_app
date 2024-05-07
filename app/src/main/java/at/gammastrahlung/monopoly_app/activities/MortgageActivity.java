package at.gammastrahlung.monopoly_app.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

import java.util.List;
import java.util.stream.Collectors;

public class MortgageActivity extends AppCompatActivity {
    private ListView propertyListView;
    private Button mortgageButton;
    private Button unmortgageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage);

        propertyListView = findViewById(R.id.property_list_view);
        mortgageButton = findViewById(R.id.mortgage_button);
        unmortgageButton = findViewById(R.id.unmortgage_button);

        if (GameData.getGameData() != null && GameData.getGameData().getGame() != null
                && GameData.getGameData().getGame().getGameBoard() != null
                && GameData.getGameData().getGame().getGameBoard().getProperties() != null) {
            List<String> propertyNames = GameData.getGameData().getGame().getGameBoard().getProperties()
                    .stream()
                    .limit(5)
                    .map(property -> property.getName() + " - " + (property.isMortgaged() ? "Mortgaged" : "Not Mortgaged"))
                    .collect(Collectors.toList());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, propertyNames);
            propertyListView.setAdapter(adapter);
            propertyListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        } else {
            // Handle null data scenario, perhaps show an error or a message
            System.out.println("GameData, game, game board, or properties list is not initialized.");
        }

        mortgageButton.setOnClickListener(view -> {
            int position = propertyListView.getCheckedItemPosition();
            if (position != ListView.INVALID_POSITION) {
                int propertyId = GameData.getGameData().getGame().getGameBoard().getProperties().get(position).getId();
                MonopolyClient.getMonopolyClient().sendMortgageMessage(true, propertyId);
            }
        });

        unmortgageButton.setOnClickListener(view -> {
            int position = propertyListView.getCheckedItemPosition();
            if (position != ListView.INVALID_POSITION) {
                int propertyId = GameData.getGameData().getGame().getGameBoard().getProperties().get(position).getId();
                MonopolyClient.getMonopolyClient().sendMortgageMessage(false, propertyId);
            }
        });
    }

}

