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

@Getter
@Setter
public class GameData extends BaseObservable {
    @Getter
    private static final GameData gameData = new GameData();

    private GameData() {}

    @Getter
    @Setter(AccessLevel.NONE)
    private final ObservableInt gameId = new ObservableInt();

    private Player player = new Player();

    @Bindable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        notifyPropertyChanged(BR.player);
    }

    private Game game;

    @Bindable
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        notifyPropertyChanged(BR.game);
    }

    private Dice dice;

    @Bindable
    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
        notifyPropertyChanged(BR.dice);
    }

    @Getter
    private final ObservableArrayList<Player> players = new ObservableArrayList<>();

    @Bindable
    public ObservableArrayList<Player> getPlayers() {
        return players;
    }

    // New method to set the list of players
    public void setPlayers(ObservableArrayList<Player> newPlayers) {
        players.clear();
        players.addAll(newPlayers);
        notifyPropertyChanged(BR.players); // Notifies about the change in the player list
    }

    public static void reset() {
        // Resetting game data
//        gameData = new GameData();
    }

    @Getter
    @Setter
    private ServerMessage.MessageType lastMessageType;

    public void updatePlayerBalance(Player updatedPlayer) {
        // Updating player balance
        for (Player p : players) {
            if (p.getId().equals(updatedPlayer.getId())) {
                p.setBalance(updatedPlayer.getBalance());
                notifyPropertyChanged(BR.players); // Notify that the player list has been updated
                break;
            }
        }
    }
}