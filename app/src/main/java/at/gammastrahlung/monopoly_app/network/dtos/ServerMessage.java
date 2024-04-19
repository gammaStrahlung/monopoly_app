package at.gammastrahlung.monopoly_app.network.dtos;

import at.gammastrahlung.monopoly_app.game.Game;
import at.gammastrahlung.monopoly_app.game.Player;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServerMessage {

    /**
     * Used for matching the message to the message handler
     */
    private String messagePath;

    private MessageType type;

    private String message;

    private Player player;

    private Game game;

    public enum MessageType {
        /**
         * The operation from the client was successful
         */
        SUCCESS,

        /**
         * The operation from the client was not successful
         */
        ERROR,

        /**
         * Informational message for the client
         */
        INFO
    }
}
