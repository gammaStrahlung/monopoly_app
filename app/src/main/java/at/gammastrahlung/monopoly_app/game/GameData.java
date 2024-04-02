package at.gammastrahlung.monopoly_app.game;

import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * Singleton that contains all game data
 */
@Getter
@Setter
public class GameData {
    @Getter
    private static final GameData gameData = new GameData();

    private GameData() {}

    /**
     * The gameId of the current game
     */
    private int gameId;

    /**
     * Our player
     */
    private Player player = new Player();

    /**
     * List of players of the game
     */
    private final HashMap<UUID, Player> players = new HashMap<>();
}
