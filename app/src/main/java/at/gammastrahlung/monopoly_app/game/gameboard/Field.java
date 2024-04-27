package at.gammastrahlung.monopoly_app.game.gameboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    private int fieldId;
    private String name;
    private FieldType type;
}
