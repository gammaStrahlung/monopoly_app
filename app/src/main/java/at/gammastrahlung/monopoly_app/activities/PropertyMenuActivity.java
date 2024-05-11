package at.gammastrahlung.monopoly_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
        });
    }
}
