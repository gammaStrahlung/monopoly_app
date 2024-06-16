package at.gammastrahlung.monopoly_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_app.game.gameboard.Utility;
import at.gammastrahlung.monopoly_app.helpers.Fields;

public class WinActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        Player winner = GameData.getGameData().getGame().getWinningPlayer();
        List<Field> ownedFields = Fields.getOwnedFields(winner);

        int propertyCount = (int) ownedFields.stream().filter(field -> field instanceof Property).count();
        int railroadCount = (int) ownedFields.stream().filter(field -> field instanceof Railroad).count();
        int utilityCount = (int) ownedFields.stream().filter(field -> field instanceof Utility).count();

        ((TextView) findViewById(R.id.winnerName)).setText(getString(R.string.winningPlayer, winner.getName()));
        ((TextView) findViewById(R.id.balance)).setText(getString(R.string.winningBalance, winner.getBalance()));
        ((TextView) findViewById(R.id.propertyCount)).setText(getString(R.string.winningProperties, propertyCount));
        ((TextView) findViewById(R.id.railroadCount)).setText(getString(R.string.winningRailroads, railroadCount));
        ((TextView) findViewById(R.id.utilityCount)).setText(getString(R.string.winningUtilities, utilityCount));
    }

    // Close button returns to MainActivity
    public void closeButtonClick(View view) {

        GameData.reset();

        // Remove gameId (used for re-joining)
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        preferenceEditor.remove("gameId");
        preferenceEditor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        // Clear previous activities
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
