package at.gammastrahlung.monopoly_app.helpers;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.FieldType;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;

public class FieldHelper {

    public static boolean isProperty(Field field) {
        return field.getType() == FieldType.PROPERTY;
    }

    public static boolean isOwnedByBank(Field field) {
        if (field instanceof Property) {
            Property property = (Property) field;
            Player bank = GameData.getGameData().getGame().getGameBoard().getBank();
            return property.getOwner().equals(bank); // belongs to the bank if owner is the bank player
        }
        return false; // if it's not a property, it can't be owned by the bank
    }

    public static boolean isPlayerOnField(Player player, Field field) {
        return player.getCurrentFieldIndex() == field.getFieldId();
    }

    public static boolean isAnyPlayerOnField(Field field) {
        for (Player player : GameData.getGameData().getPlayers()) {
            if (isPlayerOnField(player, field)) {
                return true;
            }
        }
        return false;
    }
}