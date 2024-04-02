package at.gammastrahlung.monopoly_app.network.dtos;

import com.google.gson.annotations.Expose;

import at.gammastrahlung.monopoly_app.game.Player;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClientMessage {

    /**
     * Used for matching the message to the message handler
     */
    @Expose
    private String messagePath;

    @Expose
    private String message;

    @Expose
    private Player player;
}
