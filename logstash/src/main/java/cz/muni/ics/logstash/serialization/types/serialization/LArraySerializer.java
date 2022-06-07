package cz.muni.ics.logstash.serialization.types.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cz.muni.ics.logstash.types.LArray;

import java.lang.reflect.Type;

public class LArraySerializer implements JsonSerializer<LArray> {

    @Override
    public JsonElement serialize(LArray array, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        array.forEach(item -> {
            jsonArray.add(item.toString());
        });
        return jsonArray;
    }
}
