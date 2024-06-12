package at.gammastrahlung.monopoly_app.game;

import androidx.annotation.NonNull;

import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Bid {
    private UUID playerId; // The ID of the player who made the bid
    private int amount; // The amount of the bid
    private int fieldindex; // The field the bid is made on

    @NonNull

    public String toStringAmount() {
        return String.valueOf(amount);
    }
}