package at.gammastrahlung.monopoly_app.game;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {

    // The gameId of the game
    @Getter
    private int gameId;

    // Current state of the game
    @Expose
    private GameState state;

    // The owner of the game. This is the only player that can start and end the game
    @Expose
    private Player gameOwner;

    public enum GameState {
        /**
         * Game was started but playing has not yet begun.
         * In this state, the game will accept joining players.
         */
        STARTED,
        /**
         * The game has started and players are playing the game.
         * It is not possible for new players to join the game,
         * players that disconnected are still able to re-join the game.
         */
        PLAYING,
        /**
         * The game has ended, playing and joining is not possible anymore.
         */
        ENDED
    }
}
