package at.gammastrahlung.monopoly_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import at.gammastrahlung.monopoly_app.R;

public class PropertyMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.property_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.property_name), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set OnClickListener for Button button, which opens the PropertyActionsMenue
        Button propertyActionsMenu = findViewById(R.id.button);
        propertyActionsMenu.setOnClickListener(v -> {
            // Create an intent to start the PropertyActionsMenu
            Intent intent = new Intent(PropertyMenuActivity.this, PropertyActionsMenu.class);
            // Start the PropertyActionsMenu
            startActivity(intent);

            TextView propertyName = findViewById(R.id.property_name);
            TextView currentRent = findViewById(R.id.current_rent);
            TextView fullStackRent = findViewById(R.id.full_stack_rent);
            TextView houseCount = findViewById(R.id.house_count);
            TextView hotelRent = findViewById(R.id.hotel_rent);
            TextView hotelCount = findViewById(R.id.hotel_count);
            TextView baseRent = findViewById(R.id.base_rent);
            TextView oneHouseRent = findViewById(R.id.one_house_rent);
            TextView twoHouseRent = findViewById(R.id.two_house_rent);
            TextView threeHouseRent = findViewById(R.id.three_house_rent);
            TextView fourHouseRent = findViewById(R.id.four_house_rent);
            TextView price = findViewById(R.id.price);
            TextView owner = findViewById(R.id.owner);
            ImageView buildingSlotOne = findViewById(R.id.buildingSlot1);
            ImageView buildingSlotTwo = findViewById(R.id.buildingSlot2);
            ImageView buildingSlotThree = findViewById(R.id.buildingSlot3);
            ImageView buildingSlotFour = findViewById(R.id.buildingSlot4);




        });


    }
}
