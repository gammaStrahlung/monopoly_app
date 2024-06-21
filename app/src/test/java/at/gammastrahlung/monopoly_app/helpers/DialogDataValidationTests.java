package at.gammastrahlung.monopoly_app.helpers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DialogDataValidationTests {

    @ParameterizedTest
    @ValueSource(strings = {"", "abc", "/*-+", "!@#$", "ABCDEF", "-100000", "-1", "0", "100",
            "99999", "100.0", "100000.0"})
    void validateGameIdInvalid(String input) {

        assertFalse(DialogDataValidation.validateGameId(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "100000", "999999"})
    void validateGameIdValid(String input) {

        assertTrue(DialogDataValidation.validateGameId(input));
    }

    @Test
    void validatePlayerName() {
        // Player name can be anything but empty

        assertFalse(DialogDataValidation.validatePlayerName(""));

        assertTrue(DialogDataValidation.validatePlayerName("123456"));
        assertTrue(DialogDataValidation.validatePlayerName("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(DialogDataValidation.validatePlayerName("This player has a great name"));
        assertTrue(DialogDataValidation.validatePlayerName("Player 1"));
        assertTrue(DialogDataValidation.validatePlayerName("John Doe"));
        assertTrue(DialogDataValidation.validatePlayerName("!@#$%^&*()_+-=<>,./"));
    }

    @Test
    void validateRoundAmount() {
        // Round amount has to be greater than 2

        assertFalse(DialogDataValidation.validateRoundAmount(""));
        assertFalse(DialogDataValidation.validateRoundAmount("abcdefg"));
        assertFalse(DialogDataValidation.validateRoundAmount("1"));
        assertFalse(DialogDataValidation.validateRoundAmount("-100"));
        assertFalse(DialogDataValidation.validateRoundAmount("2"));

        assertTrue(DialogDataValidation.validateRoundAmount("3"));
        assertTrue(DialogDataValidation.validateRoundAmount("10"));
        assertTrue(DialogDataValidation.validateRoundAmount("30"));
    }
}
