package at.gammastrahlung.monopoly_app.game.gameboard;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Utility extends Field {
    private Player owner;
    private int toPay;
    private int price;
    private int mortgage;
}
