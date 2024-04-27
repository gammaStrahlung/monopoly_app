package at.gammastrahlung.monopoly_app.game.gameboard;

public enum PropertyColor {
    BROWN,
    LIGHT_BLUE,
    PINK,
    ORANGE,
    RED,
    YELLOW,
    GREEN,
    DARK_BLUE;

    public String getColorString() {
        switch (this) {
            case BROWN:
                return "#5C2117";
            case LIGHT_BLUE:
                return "#9EF4FF";
            case PINK:
                return "#FF00FF";
            case ORANGE:
                return "#FFAA00";
            case RED:
                return "#FF0000";
            case YELLOW:
                return "#FFFF00";
            case GREEN:
                return "#00AA00";
            case DARK_BLUE:
                return "#0000FF";
            default:
                return null;
        }
    }
}
