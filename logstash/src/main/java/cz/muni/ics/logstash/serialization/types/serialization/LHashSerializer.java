package cz.muni.ics.logstash.serialization.types.serialization;

import com.google.gson.*;
import cz.muni.ics.logstash.types.LHash;

import java.lang.reflect.Type;

public class LHashSerializer implements JsonSerializer<LHash> {

    @Override
    public JsonElement serialize(LHash lHash, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        lHash.forEach((key, value) -> jsonObject.add(key.toString(), new JsonPrimitive(value.toString())));
        return jsonObject;
    }
}
