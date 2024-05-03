package at.gammastrahlung.monopoly_app.helpers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import at.gammastrahlung.monopoly_app.game.Game;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.Player;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_app.game.gameboard.TaxField;
import at.gammastrahlung.monopoly_app.game.gameboard.Utility;

class FieldsTest {

    private static ArrayList<Player> players;
    private static ArrayList<Field> fields;

    /**
     * Initializes fields such that every player owns the field at id (i % 4)
     */
    @BeforeAll
    static void initialize() {
        players = new ArrayList<>();

        // Generate five players
        for (int i = 0; i < 5; i++) {
            Player p = Mockito.mock(Player.class);
            Mockito.when(p.getId()).thenReturn(UUID.randomUUID());
            Mockito.when(p.getName()).thenReturn("Player " + i);
            players.add(p);
        }

        fields = new ArrayList<>();
        // Generate five Properties (can be owned)
        for (int i = 0; i < 5; i++) {
            Property p = Mockito.mock(Property.class);
            Mockito.when(p.getOwner()).thenReturn(players.get(i));
            Mockito.when(p.getName()).thenReturn("Property" + 1);
            fields.add(p);
        }

        // Generate five Railroads (can be owned)
        for (int i = 0; i < 5; i++) {
            Railroad r = Mockito.mock(Railroad.class);
            Mockito.when(r.getOwner()).thenReturn(players.get(i));
            Mockito.when(r.getName()).thenReturn("Railroad" + 1);
            fields.add(r);
        }

        // Generate five Utilities (can be owned)
        for (int i = 0; i < 5; i++) {
            Utility u = Mockito.mock(Utility.class);
            Mockito.when(u.getOwner()).thenReturn(players.get(i));
            Mockito.when(u.getName()).thenReturn("Utility" + 1);
            fields.add(u);
        }

        // Generate five Fields (can NOT be owned)
        for (int i = 0; i < 5; i++) {
            Field f = Mockito.mock(Field.class);
            Mockito.when(f.getName()).thenReturn("Field" + 1);
            fields.add(f);
        }

        // Generate five TaxFields (can NOT be owned)
        for (int i = 0; i < 5; i++) {
            TaxField t = Mockito.mock(TaxField.class);
            Mockito.when(t.getName()).thenReturn("TaxField" + 1);
            fields.add(t);
        }

        // Initialize GameData mock objects
        GameBoard gameBoard = Mockito.mock(GameBoard.class);
        Mockito.when(gameBoard.getGameBoard()).thenReturn(fields.toArray(new Field[0]));

        Game g = Mockito.mock(Game.class);
        Mockito.when(g.getGameBoard()).thenReturn(gameBoard);

        GameData.reset();
        GameData.getGameData().setGame(g);
    }

    @Test
    void property() {
        ArrayList<Property> properties = new ArrayList<>();
        for (Object f : fields.stream().filter(field -> field.getClass() == Property.class).toArray())
            properties.add((Property) f);

        for (Player p : players) {
            List<Field> ownedFields = Fields.getOwnedFields(p);

            for (Property property : properties) {
                if (property.getOwner().equals(p))
                    assertTrue(ownedFields.contains(property));
                else
                    assertFalse(ownedFields.contains(property));
            }
        }
    }

    @Test
    void railroad() {
        ArrayList<Railroad> railroads = new ArrayList<>();
        for (Object f : fields.stream().filter(field -> field.getClass() == Railroad.class).toArray())
            railroads.add((Railroad) f);

        for (Player p : players) {
            List<Field> ownedFields = Fields.getOwnedFields(p);

            for (Railroad railroad : railroads) {
                if (railroad.getOwner().equals(p))
                    assertTrue(ownedFields.contains(railroad));
                else
                    assertFalse(ownedFields.contains(railroad));
            }
        }
    }

    @Test
    void utility() {
        ArrayList<Utility> utilities = new ArrayList<>();
        for (Object f : fields.stream().filter(field -> field.getClass() == Utility.class).toArray())
            utilities.add((Utility) f);

        for (Player p : players) {
            List<Field> ownedFields = Fields.getOwnedFields(p);

            for (Utility utility : utilities) {
                if (utility.getOwner().equals(p))
                    assertTrue(ownedFields.contains(utility));
                else
                    assertFalse(ownedFields.contains(utility));
            }
        }
    }

    @Test
    void field() {
        ArrayList<Field> fields1 = new ArrayList<>();
        for (Object f : fields.stream().filter(field -> field.getClass() == Field.class).toArray())
            fields1.add((Field) f);

        for (Player p : players) {
            List<Field> ownedFields = Fields.getOwnedFields(p);
            for (Field field : fields1) {
                assertFalse(ownedFields.contains(field));
            }
        }
    }

    @Test
    void taxField() {
        ArrayList<TaxField> taxFields = new ArrayList<>();
        for (Object f : fields.stream().filter(field -> field.getClass() == TaxField.class).toArray())
            taxFields.add((TaxField) f);

        for (Player p : players) {
            List<Field> ownedFields = Fields.getOwnedFields(p);
            for (TaxField taxField : taxFields) {
                assertFalse(ownedFields.contains(taxField));
            }
        }
    }
}
