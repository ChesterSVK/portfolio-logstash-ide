package cz.muni.ics.logstash.serialization.types.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cz.muni.ics.logstash.types.LString;

import java.lang.reflect.Type;

public class LStringSerializer implements JsonSerializer<LString> {

    @Override
    public JsonElement serialize(LString lString, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(lString.toString());
    }
}
