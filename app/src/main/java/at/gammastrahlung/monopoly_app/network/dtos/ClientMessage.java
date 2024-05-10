package at.gammastrahlung.monopoly_app.network.dtos;

import at.gammastrahlung.monopoly_app.game.Player;
import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientMessage {
    @Expose
    private String messagePath;

    @Expose
    private String message;

    @Expose
    private Player player;

    @Expose
    private int propertyId;  // This will be included in the builder
    @Expose
    private String targetPlayerId;
}
