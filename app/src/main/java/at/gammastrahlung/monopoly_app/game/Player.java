// Player.java
package at.gammastrahlung.monopoly_app.game;

import com.google.gson.annotations.Expose;

import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor

public class Player {
    @Expose
    @Getter
    @Setter
    private UUID id;
    @Expose
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int currentFieldIndex;
    @Getter
    @Setter
    private int balance;


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) return false;

        return id.equals(((Player) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


}
