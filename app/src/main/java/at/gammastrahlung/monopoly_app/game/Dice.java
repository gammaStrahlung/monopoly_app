package at.gammastrahlung.monopoly_app.game;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Dice {

    @Expose
    private int value1;

    @Expose
    private int value2;

}
