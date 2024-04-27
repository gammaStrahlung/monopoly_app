package at.gammastrahlung.monopoly_app.game;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Player {
    /**
     * The unique ID of the player, this can be used by the player to allow for re-joining the game
     */
    @Expose
    private UUID id;

    /**
     * The name of the player (this is shown to other players)
     */
    @Expose
    private String name;

    private int balance;

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Player))
            return false;

        return id.equals(((Player) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
