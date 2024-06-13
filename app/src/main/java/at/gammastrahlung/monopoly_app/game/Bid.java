package at.gammastrahlung.monopoly_app.game;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Bid {
    @Getter
    @Setter

    private static UUID playerId;

    @Getter
    @Setter

    private static int amount;

    @Getter
    @Setter

    private static int fieldIndex;

    @Getter
    @Setter
    private static int wait = 0;

    @NonNull
    public static String toStringAmount() {
        return String.valueOf(amount);
    }
}
