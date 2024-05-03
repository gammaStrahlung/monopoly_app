package at.gammastrahlung.monopoly_app.helpers;

import java.util.ArrayList;

import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_app.game.gameboard.Utility;

public class Fields {
    public static ArrayList<Field> getOwnedFields (Player player) {
        ArrayList<Field> ownedFields = new ArrayList<>();

        for (Field field : GameData.getGameData().getGame().getGameBoard().getGameBoard()) {
            if (field instanceof Property && ((Property) field).getOwner().equals(player))
                ownedFields.add(field);
            else if (field instanceof Railroad && ((Railroad) field).getOwner().equals(player))
                ownedFields.add(field);
            else if (field instanceof Utility && ((Utility) field).getOwner().equals(player))
                ownedFields.add(field);
        }

        return ownedFields;
    }
}
