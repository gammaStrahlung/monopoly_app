package at.gammastrahlung.monopoly_app.game.gameboard;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameBoard {
    private Player bank;
    private Field[] gameBoard;
    private static int gameBoardSize;
    private static String FULL_SET;
    private static String HOTEL;
}
