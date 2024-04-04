package at.gammastrahlung.monopoly_app.game;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.UUID;

public class PlayerTest {

    @Test
    public void newPlayer() {
        // new player has no values
        Player p1 = new Player();
        assertNull(p1.getID());
        assertNull(p1.getName());

        UUID uuid = UUID.randomUUID();

        Player p2 = new Player(uuid, "NAME");
        assertEquals(uuid, p2.getID());
        assertEquals("NAME", p2.getName());
    }

    @Test
    public void id() {
        Player p1 = new Player();

        UUID uuid = UUID.randomUUID();
        p1.setID(uuid);
        assertEquals(uuid, p1.getID());
    }

    @Test
    public void name() {
        Player p1 = new Player();

        p1.setName("Some Name");
        assertEquals("Some Name", p1.getName());
    }
}
