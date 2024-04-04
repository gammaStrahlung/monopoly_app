package at.gammastrahlung.monopoly_app.helpers;

public class DialogDataValidation {
    private static final int minGameId = 100000;
    private static final int maxGameId = 999999;

    public static boolean validateGameId(String gameId) {
        try {
            int gameIdInt = Integer.parseInt(gameId);

            // minGameId <= gameId <= maxGameId
            return gameIdInt >= minGameId && gameIdInt <= maxGameId;
        } catch (NumberFormatException e) {
            // Input string contains characters that can not be converted into int
            return false;
        }
    }

    public static boolean validatePlayerName(String playerName) {
        // Player name is not empty
        return !playerName.isEmpty();
    }
}
