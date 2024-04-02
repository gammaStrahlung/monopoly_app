package at.gammastrahlung.monopoly_app.game;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Player {
    /**
     * The unique ID of the player, this can be used by the player to allow for re-joining the ga
     */
    @Expose
    protected UUID ID;

    /**
     * The name of the player (this is shown to other players)
     */
    @Expose
    protected String name;
}
