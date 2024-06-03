package at.gammastrahlung.monopoly_app.helpers;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;

public class MortgageHelper {

    /**
     * Manages the mortgaging and unmortgaging of properties.
     *
     * @param property The property to mortgage or unmortgage.
     * @param mortgage True to mortgage, false to unmortgage.
     * @return A string message indicating the result of the operation.
     */
    public static String manageMortgage(Property property, boolean mortgage) {
        Player currentPlayer = GameData.getGameData().getPlayer();

        if (mortgage) {
            if (!property.isMortgaged()) {
                currentPlayer.setBalance(currentPlayer.getBalance() + property.getMortgageValue());
                property.setMortgaged(true);
                return "Property mortgaged. New balance: $" + currentPlayer.getBalance();
            } else {
                return "Property is already mortgaged.";
            }
        } else {
            if (property.isMortgaged()) {
                int unmortgageCost = (int) (property.getMortgageValue() * 1.1); // 10% fee
                if (currentPlayer.getBalance() >= unmortgageCost) {
                    currentPlayer.setBalance(currentPlayer.getBalance() - unmortgageCost);
                    property.setMortgaged(false);
                    return "Property unmortgaged. New balance: $" + currentPlayer.getBalance();
                } else {
                    return "Insufficient balance to unmortgage.";
                }
            } else {
                return "Property is not mortgaged.";
            }
        }
    }
}
