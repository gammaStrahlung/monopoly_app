package at.gammastrahlung.monopoly_app.game.gameboard;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameBoard {
    private Player bank;
    private Field[] fields;
    private int gameBoardSize;
    private String fullSet;
    private String hotel;
}
