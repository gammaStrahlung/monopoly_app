package at.gammastrahlung.monopoly_app.helpers;

public class DialogDataValidation {
    private static final int MIN_GAME_ID = 100000;
    private static final int MAX_GAME_ID = 999999;

    private static final int MIN_ROUND_AMOUNT = 2;

    private DialogDataValidation() {}

    public static boolean validateGameId(String gameId) {
        try {
            int gameIdInt = Integer.parseInt(gameId);

            // minGameId <= gameId <= maxGameId
            return gameIdInt >= MIN_GAME_ID && gameIdInt <= MAX_GAME_ID;
        } catch (NumberFormatException e) {
            // Input string contains characters that can not be converted into int
            return false;
        }
    }

    public static boolean validatePlayerName(String playerName) {
        // Player name is not empty
        return !playerName.isEmpty();
    }

    public static boolean validateRoundAmount(String roundAmount) {
        try {
            int roundAmountInt = Integer.parseInt(roundAmount);
            return roundAmountInt > MIN_ROUND_AMOUNT;
        } catch (Exception e) {
            return false;
        }
    }
}
