package at.gammastrahlung.monopoly_app.game;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;

import lombok.AccessLevel;
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
    @Setter(AccessLevel.NONE)
    private ObservableInt gameId = new ObservableInt();

    /**
     * Our player
     */
    private Player player = new Player();

    /**
     * List of players of the game
     */
    private final ObservableArrayList<Player> players = new ObservableArrayList<>();
}
