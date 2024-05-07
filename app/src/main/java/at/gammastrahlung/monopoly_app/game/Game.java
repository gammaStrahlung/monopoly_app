package at.gammastrahlung.monopoly_app.game;

import androidx.databinding.ObservableArrayList;
import at.gammastrahlung.monopoly_app.game.gameboard.GameBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Dies wird einen Konstruktor mit allen Feldern als Parameter generieren.
public class Game {

    private static Game instance; // Static instance

    private int gameId;
    private GameState state;
    private Player gameOwner;
    private GameBoard gameBoard;
    private ObservableArrayList<Player> players;
    private Dice dice;

    public static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public enum GameState {
        STARTED, // Game started, players can join
        PLAYING, // Game in progress, no new players can join
        ENDED    // Game ended, no activities possible
    }
}
