package at.gammastrahlung.monopoly_app.game;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Bid {
    @Getter
    @Setter
    @Expose
    private UUID playerId;
    @Getter
    @Setter
    @Expose
    private Player playerForBid;

    @Getter
    @Setter
    @Expose
    private int amount;

    @Getter
    @Setter
    @Expose
    private int fieldIndex;


    @NonNull
    public String toStringAmount() {
        return String.valueOf(amount);
    }
}
