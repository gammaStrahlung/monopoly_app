package at.gammastrahlung.monopoly_app.game;

import androidx.annotation.NonNull;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Bid {
    @Getter
    @Setter

    private Player playerForBid;

    @Getter
    @Setter

    private int amount;

    @Getter
    @Setter

    private int fieldIndex;


    @NonNull
    public String toStringAmount() {
        return String.valueOf(amount);
    }
}
