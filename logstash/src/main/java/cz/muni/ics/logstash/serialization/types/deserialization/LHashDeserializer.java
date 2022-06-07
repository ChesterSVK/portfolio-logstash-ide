package cz.muni.ics.logstash.serialization.types.deserialization;

import com.google.gson.*;
import cz.muni.ics.logstash.types.LHash;
import cz.muni.ics.logstash.types.LString;

import java.lang.reflect.Type;

public class LHashDeserializer implements JsonDeserializer<LHash> {

    @Override
    public LHash deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        LHash lHash = new LHash();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.keySet().forEach(key -> {
            lHash.put(new LString(key), new LString(jsonObject.get(key).getAsString()));
        });
        return lHash;
    }
}
