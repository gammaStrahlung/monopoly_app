package at.gammastrahlung.monopoly_app.game.gameboard;

import java.util.Map;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Property extends Field {
    private int price;
    private Player owner;
    private PropertyColor color;
    private Map<Object, Integer> rentPrices;
    private int mortgageValue;
    private int houseCost;
    private int hotelCost;
    private int houseCount;
    private boolean mortgaged;
    private int id;
}
