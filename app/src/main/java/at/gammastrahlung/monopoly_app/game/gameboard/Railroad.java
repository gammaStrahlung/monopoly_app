package at.gammastrahlung.monopoly_app.game.gameboard;

import java.util.Map;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Railroad extends Field {
    private Player owner;
    private int price;
    private Map<String, Integer> rentPrices;
}
