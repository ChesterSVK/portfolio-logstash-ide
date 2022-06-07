package cz.muni.ics.logstash.serialization.types.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cz.muni.ics.logstash.types.LBool;

import java.lang.reflect.Type;

public class LBoolSerializer implements JsonSerializer<LBool> {
    @Override
    public JsonElement serialize(LBool lBool, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(lBool.isValue());
    }
}
