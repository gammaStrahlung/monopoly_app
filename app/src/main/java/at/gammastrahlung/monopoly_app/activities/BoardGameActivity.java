package at.gammastrahlung.monopoly_app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import at.gammastrahlung.monopoly_app.R;

public class BoardGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method with the saved instance state
        setContentView(R.layout.activity_board_screen); // Sets the content view of this activity to the activity_board_screen layout
    }
}
