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

        Player p2 = new Player(uuid, "NAME");
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
}
