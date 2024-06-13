package at.gammastrahlung.monopoly_app.helpers;

import java.util.UUID;

import lombok.Data;

@Data
public class BidHelper {
    private UUID playerId;
    private int amount;
    private int fieldIndex;

}