package at.gammastrahlung.monopoly_app.game;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.databinding.library.baseAdapters.BR;

import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Singleton that contains all game data
 */
public class GameData extends BaseObservable {
    @Getter
    private static GameData gameData = new GameData();


    private GameData() {
    }

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

    private Player currentPlayer;

    @Bindable
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyPropertyChanged(BR.currentPlayer);
    }

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
     * The diced values
     */
    private Dice dice;

    @Bindable
    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
        notifyPropertyChanged(BR.dice);
    }

    /**
     * List of log messages
     */
    @Getter
    @Bindable
    private final ObservableArrayList<String> logMessages = new ObservableArrayList<>();

    /**
     * Add a log message to the list of log messages
     *
     * @param logMessage The log message to add
     */
    public void addLogMessage(String logMessage) {
        logMessages.add(logMessage);
        // Notify observers that log messages have changed
        notifyPropertyChanged(BR.logMessages);
    }

    /**
     * List of players of the game
     */
    @Getter
    private final ObservableArrayList<Player> players = new ObservableArrayList<>();


    /**
     * Used for re-joining to check if a game exists
     */
    private Game.GameState gameState = null;

    @Bindable
    public Game.GameState getGameState() {
        return gameState;
    }

    public void setGameState(Game.GameState gameState) {
        this.gameState = gameState;
        notifyPropertyChanged(BR.gameState);
    }

    /**
     * Resets the gameData object. This is used when the game ended and the user returns to the
     * MainActivity
     */
    public static void reset() {
        gameData = new GameData();
    }

    @Getter
    @Setter
    ServerMessage.MessageType lastMessageType;

    private boolean webSocketConnected = false;

    @Bindable
    public boolean isWebSocketConnected() {
        return webSocketConnected;
    }

    public void setWebSocketConnected(boolean webSocketConnected) {
        this.webSocketConnected = webSocketConnected;
        notifyPropertyChanged(BR.webSocketConnected);
    }


}
