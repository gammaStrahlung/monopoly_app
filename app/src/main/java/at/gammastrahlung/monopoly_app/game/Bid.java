package at.gammastrahlung.monopoly_app.game;

import lombok.Data;
import java.util.UUID;

@Data
public class Bid {
    private UUID playerId; // The ID of the player who made the bid
    private int amount; // The amount of the bid
}