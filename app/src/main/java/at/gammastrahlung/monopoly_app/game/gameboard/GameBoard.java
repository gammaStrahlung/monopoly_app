package at.gammastrahlung.monopoly_app.game.gameboard;

import java.util.List;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameBoard {
    private Player bank;
    private Field[] gameBoard;
    private int GAME_BOARD_SIZE;
    private String FULL_SET;
    private String HOTEL;
    private List<Property> properties;  // Changed from an array to a list for easier management


        // Ensure there's a method to get properties
        public List<Property> getProperties() {
            return properties;
        }
    }


