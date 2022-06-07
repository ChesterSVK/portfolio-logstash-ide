package cz.muni.ics.logstash.serialization.types.deserialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import cz.muni.ics.logstash.types.LArray;
import cz.muni.ics.logstash.types.LString;

import java.lang.reflect.Type;

public class LArrayDeserializer implements JsonDeserializer<LArray> {

    @Override
    public LArray deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        LArray lArray = new LArray();
        jsonElement.getAsJsonArray().forEach(element -> lArray.add(new LString(element.getAsString())));
        return lArray;
    }
}
