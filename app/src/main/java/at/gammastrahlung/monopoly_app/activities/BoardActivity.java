package at.gammastrahlung.monopoly_app.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.fragments.AuctionDialogFragment;
import at.gammastrahlung.monopoly_app.fragments.FieldFragment;
import at.gammastrahlung.monopoly_app.fragments.FieldInfoFragment;
import at.gammastrahlung.monopoly_app.fragments.PlayerListFragment;
import at.gammastrahlung.monopoly_app.fragments.PurchaseDialogFragment;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.helpers.FieldHelper;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;

public class BoardActivity extends AppCompatActivity implements SensorEventListener {

    private ConstraintLayout fieldRowTop;
    private ConstraintLayout fieldRowBottom;
    private ConstraintLayout fieldRowLeft;
    private ConstraintLayout fieldRowRight;
    private ConstraintLayout boardLayout;
    private TextView moneyText;
    private ImageView dice1;
    private ImageView dice2;
    private Button rollDiceButton;
    private Button endTurnButton;
    private TextView playerOnTurn;
    private static final int THRESHOLD = 1000;
    private long lastTime;
    private float lastX, lastY, lastZ;
    private ArrayList<FieldFragment> fieldFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

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
        // **ADDED**: Mortgage button setup
        Button mortgageButton = findViewById(R.id.mortgage_button);
        mortgageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMortgageManagementFragment();
            }
        });
        buildGameBoard();
        updatePlayerInfo();
        updatePlayerOnTurn();

        GameData.getGameData().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.game) {
                    runOnUiThread(() -> {
                        if (fieldFragments.isEmpty()) {
                            buildGameBoard();
                        } else {
                            updateGameBoard();
                        }
                        updatePlayerInfo();
                    });
                } else if (propertyId == BR.currentPlayer) {
                    runOnUiThread(BoardActivity.this::updatePlayerOnTurn);
                } else if (propertyId == BR.dice) {
                    runOnUiThread(() -> {
                        updateDices();
                        moveAvatar();
                        enableUserActions();
                    });
                }
            }
        });

        GameData.getGameData().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.game || propertyId == BR.dice) {
                    runOnUiThread(BoardActivity.this::updateGameUI);
                }
            }
        });
        Property property = new Property();
        property.setName("Test");
        handleProperty(1,   property);

    }



    private void updateGameUI() {
        // Update game board and UI based on game state changes
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

    private void updatePlayerOnTurn() {
        Player player = GameData.getGameData().getCurrentPlayer();
        if (player == null) return;

        playerOnTurn.setText(getString(R.string.player_on_turn, player.getName()));
        rollDiceButton.setEnabled(isMyTurn());
    }

    private void updateGameBoard() {
        Field[] fields = GameData.getGameData().getGame().getGameBoard().getFields();
        ObservableArrayList<Player> players = GameData.getGameData().getPlayers();

        for (int i = 0; i < fields.length; i++) {
            int fieldOrientation = ((fields[i].getFieldId() >= 1 && fields[i].getFieldId() <= 9) || (fields[i].getFieldId() >= 21 && fields[i].getFieldId() <= 29)) ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL;

            fieldFragments.get(i).setPlayers(generatePlayerArray(players, i), fieldOrientation);
        }
    }

    private void buildGameBoard() {
        GameBoard board = GameData.getGameData().getGame().getGameBoard();
        if (board == null) return;

        fieldRowTop.removeAllViews();
        fieldRowBottom.removeAllViews();
        fieldRowLeft.removeAllViews();
        fieldRowRight.removeAllViews();

        int boardSideLength = board.getGameBoardSize() / 4;
        int horizontalFieldCount = boardSideLength + 2;
        int verticalFieldCount = boardSideLength - 2;

        for (int i = horizontalFieldCount - 2; i >= 0; i--) {
            boolean isEdge = i == 0 || i == horizontalFieldCount - 2;
            addFieldToBoard(fieldRowBottom, board.getFields()[i], isEdge, i);
        }
        addConstraints(fieldRowBottom);

        for (int i = horizontalFieldCount + verticalFieldCount - 1; i >= horizontalFieldCount - 1; i--)
            addFieldToBoard(fieldRowLeft, board.getFields()[i], false, i);
        addConstraints(fieldRowLeft);

        for (int i = horizontalFieldCount + verticalFieldCount; i < horizontalFieldCount * 2 + verticalFieldCount - 1; i++) {
            boolean isEdge = i == horizontalFieldCount + verticalFieldCount || i == horizontalFieldCount * 2 + verticalFieldCount - 2;
            addFieldToBoard(fieldRowTop, board.getFields()[i], isEdge, i);
        }
        addConstraints(fieldRowTop);

        for (int i = horizontalFieldCount * 2 + verticalFieldCount - 1; i < board.getFields().length; i++)
            addFieldToBoard(fieldRowRight, board.getFields()[i], false, i);
        addConstraints(fieldRowRight);

        getSupportFragmentManager().executePendingTransactions();
        for (int i = 0; i < board.getFields().length; i++) {
            fieldFragments.add((FieldFragment) getSupportFragmentManager().findFragmentByTag("FIELD" + i));
        }
    }

    private int[] generatePlayerArray(ObservableArrayList<Player> playerList, int fieldId) {
        int[] players = countPlayersOnField(playerList, fieldId) > 0 ? new int[countPlayersOnField(playerList, fieldId)] : null;

        if (players != null) {
            int temp = 0;
            for (int i = 0; i < players.length; i++) {
                for (int j = temp; j < playerList.size(); j++) {
                    if (fieldId == playerList.get(j).getCurrentFieldIndex()) {
                        players[i] = j + 1;
                        break;
                    }
                    if (j == playerList.size() - 1) {
                        break;
                    }
                }
                temp = players[i];
            }
        }

        return players;
    }

    private void addFieldToBoard(ConstraintLayout fieldRow, Field field, boolean isEdge, int fieldId) {
        ObservableArrayList<Player> playerList = GameData.getGameData().getPlayers();
        int[] players = generatePlayerArray(playerList, fieldId);

        if (field.getClass() == Property.class) {
            Property p = (Property) field;
            addFieldToBoard(fieldRow, p.getName(), players, true, p.getColor().getColorString(), isEdge, fieldId);
        } else {
            addFieldToBoard(fieldRow, field.getName(), players, false, null, isEdge, fieldId);
        }
    }

    private void addFieldToBoard(ConstraintLayout fieldRow, String fieldTitle, int[] playerIds, boolean showColorBar, String colorBarColor, boolean isEdge, int fieldId) {
        Bundle bundle = new Bundle();
        int colorBarPosition = FieldFragment.COLOR_BAR_NONE;
        int fieldOrientation = (fieldRow == fieldRowBottom || fieldRow == fieldRowTop) ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL;
        int height = 0;
        int width = 0;

        if (fieldRow == fieldRowTop || fieldRow == fieldRowBottom) {
            height = fieldRow.getLayoutParams().height;
            if (isEdge) width = fieldRowLeft.getLayoutParams().width;
        } else {
            width = fieldRow.getLayoutParams().width;
        }

        if (showColorBar) {
            colorBarPosition = (fieldRow == fieldRowTop) ? FieldFragment.COLOR_BAR_BOTTOM : (fieldRow == fieldRowBottom) ? FieldFragment.COLOR_BAR_TOP : (fieldRow == fieldRowLeft) ? FieldFragment.COLOR_BAR_RIGHT : FieldFragment.COLOR_BAR_LEFT;
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
        layout.setOnClickListener(v -> new FieldInfoFragment(GameData.getGameData().getGame().getGameBoard().getFields()[fieldId]).show(getSupportFragmentManager(), "FieldInfo"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().setReorderingAllowed(true).add(layout.getId(), FieldFragment.class, bundle, "FIELD" + fieldId).addToBackStack(null).commit();
    }

    private void addConstraints(ConstraintLayout fieldRow) {
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

    public void updateDices() {
        int value1 = getResources().getIdentifier("die_" + GameData.getGameData().getDice().getValue1(), "drawable", "at.gammastrahlung.monopoly_app");
        int value2 = getResources().getIdentifier("die_" + GameData.getGameData().getDice().getValue2(), "drawable", "at.gammastrahlung.monopoly_app");

        dice1.setImageResource(value1);
        dice2.setImageResource(value2);
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

    public void rollDiceClick(View v) {
        rollDice();
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 100) {
                long diffTime = currentTime - lastTime;
                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

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

    public int countPlayersOnField(ObservableArrayList<Player> list, int field) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (field == list.get(i).getCurrentFieldIndex()) {
                count++;
            }
        }
        return count;
    }

    public void moveAvatar() {
        MonopolyClient.getMonopolyClient().moveAvatar();
    }



    public void showAuctionDialog(String propertyName, int propertyId) {
        DialogFragment auctionDialog = AuctionDialogFragment.newInstance(propertyName, propertyId);
        auctionDialog.show(getSupportFragmentManager(), "auctionDialog");
    }

    private void showPurchaseDialog(String propertyName, int propertyId) {
        PurchaseDialogFragment dialog = PurchaseDialogFragment.newInstance(propertyName, propertyId);
        dialog.setPurchaseDialogListener(new PurchaseDialogFragment.PurchaseDialogListener() {
            @Override
            public void onNoButtonClicked() {
                showAuctionDialog(propertyName, propertyId);
            }

            @Override
            public void onYesButtonClicked() {
                // Handle the purchase logic here
            }
        });
        dialog.show(getSupportFragmentManager(), "purchaseDialog");
    }


    private void checkAndShowPurchaseDialog() {
        Player currentPlayer = GameData.getGameData().getCurrentPlayer();
        Field currentField = GameData.getGameData().getGame().getGameBoard().getFields()[currentPlayer.getCurrentFieldIndex()];
        String currentFieldName = currentField.getName();

        List<String> propertyNames = Arrays.asList(
                "Mediterranean Avenue", "Baltic Avenue", "Oriental Avenue", "Vermont Avenue",
                "Connecticut Avenue", "St Charles Place", "States Avenue", "Virginia Avenue",
                "St. James Place", "Tennessee Avenue", "New York Avenue", "Kentucky Avenue",
                "Indiana Avenue", "Illinois Avenue", "Atlantic Avenue", "Ventnor Avenue",
                "Marvin Gardens", "Pacific Avenue", "North Carolina Avenue", "Pennsylvania Avenue",
                "Park Place", "Boardwalk"
        );

            showPurchaseDialog(currentFieldName, currentField.getFieldId());
        }


    public void handlePropertyPurchaseDecision(boolean purchase) {
        if (!purchase) {
            showAuctionDialog("Property Name", 1); // Replace with actual property name and ID
        }
    }
    private void handleProperty(int propertyId, Property property) {
        if (FieldHelper.isOwnedByBank(property)) {
            showPurchaseDialog(property.getName(), propertyId); // Füge propertyId hinzu
        } else {
            // Handle already owned property
        }
    }

    public void returnToBoard() {
        // Logic to update the board UI after returning from the auction
        updateGameBoard();
        updatePlayerOnTurn();
        Toast.makeText(this, "Returned to Board", Toast.LENGTH_SHORT).show();

    }

    private void showMortgageManagementFragment() {
        Intent intent = new Intent(this, MortgageActivity.class);
        startActivity(intent);
    }


}