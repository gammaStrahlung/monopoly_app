package at.gammastrahlung.monopoly_app.network;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_app.game.gameboard.TaxField;
import at.gammastrahlung.monopoly_app.game.gameboard.Utility;

public class FieldDeserializer implements JsonDeserializer<Field> {
    private final Gson gson = new Gson();

    @Override
    public Field deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try {
            String type = json.getAsJsonObject().get("fieldClass").getAsString();

            switch (type) {
                case "Field":
                    return gson.fromJson(json, Field.class);
                case "Property":
                    return gson.fromJson(json, Property.class);
                case "Railroad":
                    return gson.fromJson(json, Railroad.class);
                case "TaxField":
                    return gson.fromJson(json, TaxField.class);
                case "Utility":
                    return gson.fromJson(json, Utility.class);
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            throw new JsonParseException("Determining FieldType was not possible");
        }
    }
}
