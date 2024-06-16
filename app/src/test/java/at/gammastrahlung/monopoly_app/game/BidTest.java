package at.gammastrahlung.monopoly_app.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

 class BidTest {
    private Bid bid;

    @BeforeEach
    void setup() {
        bid = new Bid();
    }
    @Test
     void testGettersAndSetters() {


        // Test playerId setter and getter
        UUID testPlayerId = UUID.randomUUID();
        bid.setPlayerId(testPlayerId);
        assertEquals(testPlayerId, bid.getPlayerId(), "Player ID should match the one set.");

        // Test amount setter and getter
        int testAmount = 1500;
        bid.setAmount(testAmount);
        assertEquals(testAmount, bid.getAmount(), "Amount should match the one set.");

        // Test fieldIndex setter and getter
        int testFieldIndex = 10;
        bid.setFieldIndex(testFieldIndex);
        assertEquals(testFieldIndex, bid.getFieldIndex(), "Field index should match the one set.");


    }

    @Test
     void testToStringAmount() {

        bid.setAmount(2000);
        assertEquals("2000", bid.toStringAmount(), "toStringAmount should return a string representation of amount.");
    }

    @Test
     void testToStringAmountWithNegativeValue() {

        bid.setAmount(-100);
        assertEquals("-100", bid.toStringAmount(), "toStringAmount should handle negative values correctly.");
    }

    @Test
     void noArgsConstructor_initialState() {
        assertNotNull(bid, "Bid instance should not be null after construction.");
        assertNull(bid.getPlayerId(), "Player ID should be null by default.");
        assertEquals(0, bid.getAmount(), "Amount should be 0 by default.");
        assertEquals(0, bid.getFieldIndex(), "Field index should be 0 by default.");

    }






}
