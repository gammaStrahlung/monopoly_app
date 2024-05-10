package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.UUID;

class PlayerTest {

    @Test
    void newPlayer() {
        // new player has no values
        Player p1 = new Player();
        assertNull(p1.getId());
        assertNull(p1.getName());

        UUID uuid = UUID.randomUUID();

        Player p2 = new Player(uuid, "NAME", 0,0);
        assertEquals(uuid, p2.getId());
        assertEquals("NAME", p2.getName());
    }

    @Test
    void id() {
        Player p1 = new Player();

        UUID uuid = UUID.randomUUID();
        p1.setId(uuid);
        assertEquals(uuid, p1.getId());
    }

    @Test
    void name() {
        Player p1 = new Player();

        p1.setName("Some Name");
        assertEquals("Some Name", p1.getName());
    }

    @Test
    void balance() {
        Player p1 = new Player();

        p1.setBalance(100);
        assertEquals(100, p1.getBalance());
    }

    @Test
    void equalsTest() {
        UUID playerid = UUID.randomUUID();

        Player p1 = new Player();
        p1.setId(playerid);

        Player p2 = new Player();
        p2.setId(playerid);

        // Players with the same UUID are the same player (This is done, because the player object
        // is created on each update from the serever)
        assertEquals(p1, p2);

        // Other object is not equal
        Object a = 1;
        assertNotEquals(p1, a);
    }

    @Test
    void hashCodeTest() {
        Player p1 = new Player();
        p1.setId(UUID.randomUUID());

        Player p2 = new Player();
        p2.setId(UUID.randomUUID());

        assertNotEquals(p1.hashCode(), p2.hashCode());
    }
}
