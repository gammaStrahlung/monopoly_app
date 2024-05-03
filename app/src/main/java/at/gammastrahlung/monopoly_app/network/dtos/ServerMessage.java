package at.gammastrahlung.monopoly_app.network.dtos;

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

    private String updateType;

    private String jsonData;

    private MessageType type;

    private Player player;

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
