package at.gammastrahlung.monopoly_app.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.fragments.FieldFragment;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;

public class BoardActivity extends AppCompatActivity {

    private ConstraintLayout fieldRowTop; // Includes top corners
    private ConstraintLayout fieldRowBottom; // Includes bottom corners
    private ConstraintLayout fieldRowLeft;
    private ConstraintLayout fieldRowRight;
    private ConstraintLayout boardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method with the saved instance state
        setContentView(R.layout.activity_board); // Sets the content view of this activity to the activity_board_screen layout
    }

    @Override
    protected void onResume() {
        super.onResume();

        fieldRowTop = findViewById(R.id.field_row_top);
        fieldRowBottom = findViewById(R.id.field_row_bottom);
        fieldRowLeft = findViewById(R.id.field_row_left);
        fieldRowRight = findViewById(R.id.field_row_right);
        boardLayout = findViewById(R.id.boardLayout);

        buildGameBoard();

        // Update when game data changes
        GameData.getGameData().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId != BR.game)
                    return;

                runOnUiThread(() -> buildGameBoard());
            }
        });
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

        int boardSideLenght = board.getGameBoardSize() / 4;

        int horizontalFieldCount = boardSideLenght + 2; // Includes edges
        int verticalFieldCount = boardSideLenght - 2; // Does not include edges

        /* Monopoly game starts bottom right and then goes in clockwise direction
        [6] [7] [8] [ 9]
        [5] ->      [10]
        [4]      <- [11]
        [3] [2] [1] [ 0] (Start)
         */

        // Bottom row (right to left)
        for (int i = horizontalFieldCount - 2; i >= 0; i--) {
            boolean isEdge = i == 0 || i == horizontalFieldCount - 2;
            addFieldToBoard(fieldRowBottom, board.getGameBoard()[i], isEdge);
        }
        addConstraints(fieldRowBottom);

        // Left row (bottom to top)
        for (int i = horizontalFieldCount + verticalFieldCount - 1; i >= horizontalFieldCount - 1; i--)
            addFieldToBoard(fieldRowLeft, board.getGameBoard()[i], false);
        addConstraints(fieldRowLeft);

        // Top row (left to right)
        for (int i = horizontalFieldCount + verticalFieldCount; i < horizontalFieldCount * 2 + verticalFieldCount - 1; i++) {
            boolean isEdge = i == horizontalFieldCount + verticalFieldCount || i == horizontalFieldCount * 2 + verticalFieldCount - 2;
            addFieldToBoard(fieldRowTop, board.getGameBoard()[i], isEdge);
        }
        addConstraints(fieldRowTop);

        // Right row (top to bottom)
        for (int i = horizontalFieldCount * 2 + verticalFieldCount - 1; i < board.getGameBoardSize(); i++)
            addFieldToBoard(fieldRowRight, board.getGameBoard()[i], false);
        addConstraints(fieldRowRight);
    }

    private void addFieldToBoard(ConstraintLayout fieldRow, Field field, boolean isEdge) {

        // Add players on the field here (Not currently tracked on the server):
        int[] players = null;

        if (field.getClass() == Property.class) {
            Property p = (Property) field;
            addFieldToBoard(fieldRow, p.getName(), players, true, p.getColor().getColorString(), isEdge);

        } else {
            addFieldToBoard(fieldRow, field.getName(), players, false, null, isEdge);
        }
    }

    private void addFieldToBoard(ConstraintLayout fieldRow, String fieldTitle, int[] playerIds, boolean showColorBar, String colorBarColor, boolean isEdge) {
        Bundle bundle = new Bundle();
        int colorBarPosition = FieldFragment.COLOR_BAR_NONE;

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

        bundle.putInt("color_bar_position", colorBarPosition);
        bundle.putString("color_bar_color", colorBarColor);
        bundle.putString("title", fieldTitle);
        bundle.putIntArray("players", playerIds);

        ConstraintLayout layout = new ConstraintLayout(this);
        fieldRow.addView(layout);

        layout.getLayoutParams().height = height;
        layout.getLayoutParams().width = width;
        layout.setId(View.generateViewId());

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(layout.getId(), FieldFragment.class, bundle, "FRAG")
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
}
