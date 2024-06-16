package at.gammastrahlung.monopoly_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.adapters.PlayerAdapter;
import at.gammastrahlung.monopoly_app.fragments.FieldFragment;
import at.gammastrahlung.monopoly_app.fragments.FieldInfoFragment;
import at.gammastrahlung.monopoly_app.fragments.PlayerListFragment;
import at.gammastrahlung.monopoly_app.fragments.SelectValueFragment;
import at.gammastrahlung.monopoly_app.game.Game;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class BoardActivity extends AppCompatActivity implements SensorEventListener, SelectValueFragment.OnValueSelectedListener {

    private ConstraintLayout fieldRowTop; // Includes top corners
    private ConstraintLayout fieldRowBottom; // Includes bottom corners
    private ConstraintLayout fieldRowLeft;
    private ConstraintLayout fieldRowRight;
    private ConstraintLayout boardLayout;
    private TextView moneyText;

    private ImageView dice1;
    private ImageView dice2;

    private Button rollDiceButton;
    private Button endTurnButton;
    private TextView logTextView;
    private ScrollView logScrollView;
    private static final int THRESHOLD = 1000;
    private long lastTime;
    private float lastX, lastY, lastZ;

    private RecyclerView playersRecyclerView;
    private PlayerAdapter playerAdapter;

    private TextView playerOnTurn;

    ArrayList<FieldFragment> fieldFragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method with the saved instance state
        setContentView(R.layout.activity_board); // Sets the content view of this activity to the activity_board_screen layout

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        fieldRowTop = findViewById(R.id.field_row_top);
        fieldRowBottom = findViewById(R.id.field_row_bottom);
        fieldRowLeft = findViewById(R.id.field_row_left);
        fieldRowRight = findViewById(R.id.field_row_right);
        boardLayout = findViewById(R.id.boardLayout);

        moneyText = findViewById(R.id.moneyText);

        dice1 = findViewById(R.id.imageView1);
        dice2 = findViewById(R.id.imageView5);

        rollDiceButton = findViewById(R.id.rollDices);
        endTurnButton = findViewById(R.id.endTurn);
        playerOnTurn = findViewById(R.id.playerOnTurn);

        logTextView = findViewById(R.id.logTextView);
        logScrollView = findViewById(R.id.logScrollView);

        playersRecyclerView = findViewById(R.id.players_recycler_view);
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        playerAdapter = new PlayerAdapter(GameData.getGameData().getPlayers(), this, false, false, false, true);

        playersRecyclerView.setAdapter(playerAdapter);

        buildGameBoard();
        updatePlayerInfo();
        updatePlayerOnTurn();

        GameData.getGameData().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                // Update when game data changes
                if (propertyId == BR.game) {
                    runOnUiThread(() -> {
                        if (GameData.getGameData().getGame().getState() == Game.GameState.ENDED) {
                            GameData.getGameData().removeOnPropertyChangedCallback(this);
                            gameEnd();
                        } else if (fieldFragments.isEmpty()) {
                            buildGameBoard();
                        } else {
                            updateGameBoard();
                        }
                        updatePlayerInfo();
                    });
                } else if (propertyId == BR.player) {
                    runOnUiThread(() -> updatePlayerInfo());
                }
                // Update when player on turn changes
                else if (propertyId == BR.currentPlayer) {
                    runOnUiThread(() -> updatePlayerOnTurn());
                    runOnUiThread(() -> updatePlayerList());
                }
                else if (propertyId == BR.logMessages){
                    runOnUiThread(() -> updateLogMessages());
                }
                // Update when dice value is changed
                else if (propertyId == BR.dice) {
                    runOnUiThread(() -> {
                        updateDices();      // updating the view of each die
                        if (isMyTurn()) {
                            new SelectValueFragment().show(getSupportFragmentManager(), "selectedValue");
                        }
                        enableUserActions();
                    });
                } else if (propertyId == BR.webSocketConnected) {
                    if (!GameData.getGameData().isWebSocketConnected()) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), R.string.disconnected, Toast.LENGTH_LONG).show();
                            GameData.getGameData().removeOnPropertyChangedCallback(this);
                            finish();
                        });
                    }
                }
            }
        });
    }

    private void gameEnd() {
        if (GameData.getGameData().getGame().getWinningPlayer() != null) {
            // Somebody has won -> Open WinActivity
        } else {
            // Game was only ended and nobody has won -> Return to MainActivity

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

    private void updatePlayerList() {
        List<Player> players = GameData.getGameData().getPlayers();

        playerAdapter.updatePlayers(players);
    }

    private boolean isMyTurn() {
        return GameData.getGameData().getCurrentPlayer().equals(GameData.getGameData().getPlayer());
    }

    public void otherPlayersClick(View v) {
        new PlayerListFragment().show(getSupportFragmentManager(), "playerList");
    }

    private void updatePlayerInfo() {
        moneyText.setText(getString(R.string.money, GameData.getGameData().getPlayer().getBalance()));
    }

    private void updateLogMessages() {
        List<String> logMessages = GameData.getGameData().getLogMessages();
        StringBuilder logText = new StringBuilder();
        for (String logMessage : logMessages) {
            logText.append(logMessage).append("\n");
        }
        logTextView.setText(logText.toString());
        // Scroll ScrollView to the bottom
        logScrollView.post(() -> logScrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void updatePlayerOnTurn() {
        Player player = GameData.getGameData().getCurrentPlayer();
        if (player == null)
            return;

        playerOnTurn.setText(getString(R.string.player_on_turn, player.getName()));

        // if current player is our player then enable roll dice button
        if (isMyTurn()) {
            rollDiceButton.setEnabled(true);
        } else {
            rollDiceButton.setEnabled(false);
        }
    }

    private void updateGameBoard() {
        Field[] fields = GameData.getGameData().getGame().getGameBoard().getFields();
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();

        for (int i = 0; i < fields.length; i++) {
            int fieldOrientation;

            // how the players should be placed depending on the field location
            if ((fields[i].getFieldId() >= 1 && fields[i].getFieldId() <= 9) || (fields[i].getFieldId() >= 21 && fields[i].getFieldId() <= 29)) {
                fieldOrientation = LinearLayout.VERTICAL;
            } else {
                fieldOrientation = LinearLayout.HORIZONTAL;
            }

            fieldFragments.get(i).setPlayers(generatePlayerArray(players, i), fieldOrientation);
        }
    }

    private void buildGameBoard() {
        GameBoard board = GameData.getGameData().getGame().getGameBoard();
        if (board == null)
            return; // Don't build board if it does not exist

        // Clear views
        fieldRowTop.removeAllViews();
        fieldRowBottom.removeAllViews();
        fieldRowLeft.removeAllViews();
        fieldRowRight.removeAllViews();

        int boardSideLength = board.getGameBoardSize() / 4;

        int horizontalFieldCount = boardSideLength + 2; // Includes edges
        int verticalFieldCount = boardSideLength - 2; // Does not include edges

        /* Monopoly game starts bottom right and then goes in clockwise direction
        [6] [7] [8] [ 9]
        [5] ->      [10]
        [4]      <- [11]
        [3] [2] [1] [ 0] (Start)
         */

        // Bottom row (right to left)
        for (int i = horizontalFieldCount - 2; i >= 0; i--) {
            boolean isEdge = i == 0 || i == horizontalFieldCount - 2;
            addFieldToBoard(fieldRowBottom, board.getFields()[i], isEdge, i);
        }
        addConstraints(fieldRowBottom);

        // Left row (bottom to top)
        for (int i = horizontalFieldCount + verticalFieldCount - 1; i >= horizontalFieldCount - 1; i--)
            addFieldToBoard(fieldRowLeft, board.getFields()[i], false, i);
        addConstraints(fieldRowLeft);

        // Top row (left to right)
        for (int i = horizontalFieldCount + verticalFieldCount; i < horizontalFieldCount * 2 + verticalFieldCount - 1; i++) {
            boolean isEdge = i == horizontalFieldCount + verticalFieldCount || i == horizontalFieldCount * 2 + verticalFieldCount - 2;
            addFieldToBoard(fieldRowTop, board.getFields()[i], isEdge, i);
        }
        addConstraints(fieldRowTop);

        // Right row (top to bottom)
        for (int i = horizontalFieldCount * 2 + verticalFieldCount - 1; i < board.getGameBoardSize(); i++)
            addFieldToBoard(fieldRowRight, board.getFields()[i], false, i);
        addConstraints(fieldRowRight);

        getSupportFragmentManager().executePendingTransactions();

        for (int i = 0; i < board.getFields().length; i++) {
            fieldFragments.add((FieldFragment) getSupportFragmentManager().findFragmentByTag("FIELD" + i));
        }
    }

    private int[] generatePlayerArray(ObservableArrayList<Player> playerList, int fieldId) {
        int[] players;

        // Check if there are any players on currently build field
        if (countPlayersOnField(playerList, fieldId) > 0) {
            players = new int[countPlayersOnField(playerList, fieldId)];

            int temp = 0;
            for (int i = 0; i < players.length; i++) {
                for (int j = temp; j < playerList.size(); j++) {
                    if (fieldId == playerList.get(j).getCurrentFieldIndex()) {
                        players[i] = j + 1;     // each player with unique icon
                        break;
                    }
                    if (j == playerList.size() - 1) {
                        break;
                    }
                }
                temp = players[i];
            }
        } else {
            players = null;
        }

        return players;
    }

    private void addFieldToBoard(ConstraintLayout fieldRow, Field field, boolean isEdge, int fieldId) {

        // List of currently playing players
        ObservableArrayList<Player> playerList = GameData.getGameData().getPlayers();
        int[] players;

        players = generatePlayerArray(playerList, fieldId);

        if (field.getClass() == Property.class) {
            Property p = (Property) field;
            addFieldToBoard(fieldRow, p.getBoardName(), players, true, p.getColor().getColorString(), isEdge, fieldId);

        } else {
            addFieldToBoard(fieldRow, field.getBoardName(), players, false, null, isEdge, fieldId);
        }
    }

    private void addFieldToBoard(ConstraintLayout fieldRow, String fieldTitle, int[] playerIds, boolean showColorBar, String colorBarColor, boolean isEdge, int fieldId) {
        Bundle bundle = new Bundle();
        int colorBarPosition = FieldFragment.COLOR_BAR_NONE;
        int fieldOrientation;

        int height = 0;
        int width = 0;

        if (fieldRow == fieldRowTop || fieldRow == fieldRowBottom) {
            height = fieldRow.getLayoutParams().height;

            if (isEdge)
                width = fieldRowLeft.getLayoutParams().width;
        } else {
            width = fieldRow.getLayoutParams().width;
        }

        if (showColorBar) {
            if (fieldRow == fieldRowTop)
                colorBarPosition = FieldFragment.COLOR_BAR_BOTTOM;
            else if (fieldRow == fieldRowBottom)
                colorBarPosition = FieldFragment.COLOR_BAR_TOP;
            else if (fieldRow == fieldRowLeft)
                colorBarPosition = FieldFragment.COLOR_BAR_RIGHT;
            else if (fieldRow == fieldRowRight)
                colorBarPosition = FieldFragment.COLOR_BAR_LEFT;
        }

        if (fieldRow == fieldRowBottom || fieldRow == fieldRowTop) {
            fieldOrientation = LinearLayout.VERTICAL;
        } else {
            fieldOrientation = LinearLayout.HORIZONTAL;
        }

        bundle.putInt("color_bar_position", colorBarPosition);
        bundle.putString("color_bar_color", colorBarColor);
        bundle.putString("title", fieldTitle);
        bundle.putIntArray("players", playerIds);
        bundle.putInt("orientation", fieldOrientation);

        ConstraintLayout layout = new ConstraintLayout(this);
        fieldRow.addView(layout);

        layout.getLayoutParams().height = height;
        layout.getLayoutParams().width = width;
        layout.setId(View.generateViewId());
        layout.setOnClickListener(v -> new FieldInfoFragment(GameData.getGameData().getGame().getGameBoard().getFields()[fieldId])
                .show(getSupportFragmentManager(), "FieldInfo"));

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(layout.getId(), FieldFragment.class, bundle, "FIELD" + fieldId)
                .addToBackStack(null)
                .commit();
    }

    private void addConstraints(ConstraintLayout fieldRow) {
        // If direction is horizontal
        boolean horizontal = fieldRow == fieldRowTop || fieldRow == fieldRowBottom;

        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(fieldRow);

        int[] elements = new int[fieldRow.getChildCount()];
        for (int i = 0; i < fieldRow.getChildCount(); i++) {
            elements[i] = fieldRow.getChildAt(i).getId();

            if (horizontal) {
                constraints.connect(fieldRow.getId(), ConstraintSet.TOP, elements[i], ConstraintSet.TOP);
                constraints.connect(fieldRow.getId(), ConstraintSet.BOTTOM, elements[i], ConstraintSet.BOTTOM);
            } else {
                constraints.connect(fieldRow.getId(), ConstraintSet.START, elements[i], ConstraintSet.START);
                constraints.connect(fieldRow.getId(), ConstraintSet.END, elements[i], ConstraintSet.END);
            }
        }

        if (horizontal) {
            constraints.createHorizontalChain(fieldRow.getId(), ConstraintSet.LEFT, fieldRow.getId(), ConstraintSet.RIGHT, elements, null, ConstraintSet.CHAIN_SPREAD);
        } else {
            constraints.createVerticalChain(fieldRow.getId(), ConstraintSet.TOP, fieldRow.getId(), ConstraintSet.BOTTOM, elements, null, ConstraintSet.CHAIN_SPREAD);
        }

        constraints.applyTo(fieldRow);
        fieldRow.invalidate();
        boardLayout.invalidate();
    }

    // Updates ImageView of each die
    public void updateDices() {
        int value1 = getResources().getIdentifier("die_" + GameData.getGameData().getDice().getValue1(), "drawable", "at.gammastrahlung.monopoly_app");
        int value2 = getResources().getIdentifier("die_" + GameData.getGameData().getDice().getValue2(), "drawable", "at.gammastrahlung.monopoly_app");

        dice1.setImageResource(value1);
        dice2.setImageResource(value2);

        int dicedValue = GameData.getGameData().getDice().getValue1() + GameData.getGameData().getDice().getValue2();
    }

    public void enableUserActions() {
        rollDiceButton.setEnabled(false);
        endTurnButton.setEnabled(false);

        if (isMyTurn()) {
            endTurnButton.setEnabled(true);
        }
    }

    public void rollDice() {
        MonopolyClient.getMonopolyClient().rollDice();
    }

    // Button is clicked to roll the dice
    public void rollDiceClick(View v) {
        rollDice();
    }

    // Motion Sensor that triggers rollDice as well
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];      // acceleration force along the x axis
            float y = event.values[1];      // acceleration force along the y axis
            float z = event.values[2];      // acceleration force along the z axis

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 100) {
                long diffTime = currentTime - lastTime;
                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

                // certain speed needed to trigger event
                if (speed > THRESHOLD) {
                    rollDice();
                }
                lastTime = currentTime;
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onEndTurnButtonClicked(View view) {
        endTurnButton.setEnabled(false);
        MonopolyClient.getMonopolyClient().endCurrentPlayerTurn();
    }

    // Counts how many players are on the field that is currently being built
    public int countPlayersOnField(ObservableArrayList<Player> list, int field) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (field == list.get(i).getCurrentFieldIndex()) {
                count++;
            }
        }
        return count;
    }

    // After dice roll, the avatar will be moved
    public void moveAvatar() {
        MonopolyClient.getMonopolyClient().moveAvatar();
    }

    public void moveAvatarAfterCheating() {
        MonopolyClient.getMonopolyClient().moveAvatarAfterCheating();
    }

    // Player does not want to cheat and moves to diced value forward
    @Override
    public void onForward(int value) {
        moveAvatar();
    }

    // Player does want to cheat and moves a selected value forward
    @Override
    public void onSelectedValue(int value){
        moveAvatarAfterCheating();
    }
}
