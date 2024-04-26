package at.gammastrahlung.monopoly_app.game.gameboard;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameBoard {
    private Player bank;
    private Field[] gameBoard;
    private int gameBoardSize;
    private String FULL_SET;
    private String HOTEL;
}
