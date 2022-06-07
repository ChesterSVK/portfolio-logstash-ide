package cz.muni.ics.logstash.serialization.types.deserialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import cz.muni.ics.logstash.types.LBool;

import java.lang.reflect.Type;

public class LBoolDeserializer implements JsonDeserializer<LBool> {

    @Override
    public LBool deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        return new LBool(jsonElement.getAsBoolean());
    }
}
