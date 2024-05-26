package at.gammastrahlung.monopoly_app.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.fragments.MortgageManagementFragment;

public class MortgageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage);  // This layout needs to be created

        // Add the MortgageManagementFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MortgageManagementFragment())
                    .commit();
        }
    }
}
