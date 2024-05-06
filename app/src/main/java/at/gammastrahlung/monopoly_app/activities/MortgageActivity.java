package at.gammastrahlung.monopoly_app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class MortgageActivity extends AppCompatActivity {
    private EditText propertyIdInput;
    private Button mortgageButton;
    private Button unmortgageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage);

        propertyIdInput = findViewById(R.id.property_id_input);
        mortgageButton = findViewById(R.id.mortgage_button);
        unmortgageButton = findViewById(R.id.unmortgage_button);

        mortgageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int propertyId = Integer.parseInt(propertyIdInput.getText().toString());
                MonopolyClient.getMonopolyClient().sendMortgageMessage(true, propertyId);
            }
        });

        unmortgageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int propertyId = Integer.parseInt(propertyIdInput.getText().toString());
                MonopolyClient.getMonopolyClient().sendMortgageMessage(false, propertyId);
            }
        });
    }
}
