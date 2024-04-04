package at.gammastrahlung.monopoly_app.helpers;

import static org.junit.Assert.*;

import org.junit.Test;

public class DialogDataValidationTests {

    @Test
    public void validateGameId() {
        // Game ID has to be in range 100000 - 999999

        // Test empty
        assertFalse(DialogDataValidation.validateGameId(""));

        // Test non number input
        assertFalse(DialogDataValidation.validateGameId("abc"));
        assertFalse(DialogDataValidation.validateGameId("/*-+"));
        assertFalse(DialogDataValidation.validateGameId("!@#$"));
        assertFalse(DialogDataValidation.validateGameId("ABCDEF")); // Six digits like gameId

        // Test negative
        assertFalse(DialogDataValidation.validateGameId(String.valueOf(Integer.MIN_VALUE)));
        assertFalse(DialogDataValidation.validateGameId("-100000"));
        assertFalse(DialogDataValidation.validateGameId("-1"));

        // Test to small
        assertFalse(DialogDataValidation.validateGameId("0"));
        assertFalse(DialogDataValidation.validateGameId("100"));
        assertFalse(DialogDataValidation.validateGameId("99999"));

        // Test floating point
        assertFalse(DialogDataValidation.validateGameId("100.0"));
        assertFalse(DialogDataValidation.validateGameId("100000.0"));

        // Test valid
        assertTrue(DialogDataValidation.validateGameId("123456"));
        assertTrue(DialogDataValidation.validateGameId("100000"));
        assertTrue(DialogDataValidation.validateGameId("999999"));
    }

    @Test
    public void validatePlayerName() {
        // Player name can be anything but empty

        assertFalse(DialogDataValidation.validatePlayerName(""));

        assertTrue(DialogDataValidation.validatePlayerName("123456"));
        assertTrue(DialogDataValidation.validatePlayerName("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(DialogDataValidation.validatePlayerName("This player has a great name"));
        assertTrue(DialogDataValidation.validatePlayerName("Player 1"));
        assertTrue(DialogDataValidation.validatePlayerName("John Doe"));
        assertTrue(DialogDataValidation.validatePlayerName("!@#$%^&*()_+-=<>,./"));
    }
}
