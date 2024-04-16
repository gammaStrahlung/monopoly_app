package at.gammastrahlung.monopoly_app.game;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.databinding.library.baseAdapters.BR;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Singleton that contains all game data
 */
public class GameData extends BaseObservable {
    @Getter
    private static final GameData gameData = new GameData();

    private GameData() {}

    /**
     * The gameId of the current game
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private ObservableInt gameId = new ObservableInt();

    /**
     * Our player
     */
    private Player player = new Player();

    @Bindable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        notifyPropertyChanged(BR.player);
    }

    /**
     * The game and its data
     */
    private Game game;

    @Bindable
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        notifyPropertyChanged(BR.game);
    }

    /**
     * List of players of the game
     */
    @Getter
    private final ObservableArrayList<Player> players = new ObservableArrayList<>();
}
