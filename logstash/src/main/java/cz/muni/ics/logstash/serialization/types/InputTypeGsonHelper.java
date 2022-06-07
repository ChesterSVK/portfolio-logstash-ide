package cz.muni.ics.logstash.serialization.types;

import com.google.gson.*;
import cz.muni.ics.logstash.enums.AnonymizeAlgorithm;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.exception.LogstashMessages;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.serialization.types.deserialization.*;
import cz.muni.ics.logstash.serialization.types.serialization.*;
import cz.muni.ics.logstash.types.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

public abstract class InputTypeGsonHelper {

    private static Gson gson;

    private static JsonSerializer<LString> stringSerializer = new LStringSerializer();
    private static JsonSerializer<LArray> arraySerializer = new LArraySerializer();
    private static JsonSerializer<LHash> hashSerializer = new LHashSerializer();
    private static JsonSerializer<LBool> boolSerializer = new LBoolSerializer();

    private static JsonDeserializer<LString> stringDeserializer = new LStringDeserializer();
    private static JsonDeserializer<LArray> arrayDeserializer = new LArrayDeserializer();
    private static JsonDeserializer<LHash> hashDeserializer = new LHashDeserializer();
    private static JsonDeserializer<LBool> boolDeserializer = new LBoolDeserializer();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    static  {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LString.class, stringSerializer);
        gsonBuilder.registerTypeAdapter(LArray.class, arraySerializer);
        gsonBuilder.registerTypeAdapter(LTimestampArray.class, arraySerializer);
        gsonBuilder.registerTypeAdapter(LHash.class, hashSerializer);
        gsonBuilder.registerTypeAdapter(LBool.class, boolSerializer);
        gsonBuilder.registerTypeAdapter(LAnonymizeType.class, stringSerializer);
        gsonBuilder.registerTypeAdapter(LUri.class, stringSerializer);


        gsonBuilder.registerTypeAdapter(LString.class, stringDeserializer);
        gsonBuilder.registerTypeAdapter(LArray.class, arrayDeserializer);
        gsonBuilder.registerTypeAdapter(LTimestampArray.class, arrayDeserializer);
        gsonBuilder.registerTypeAdapter(LHash.class, hashDeserializer);
        gsonBuilder.registerTypeAdapter(LBool.class, boolDeserializer);
        gsonBuilder.registerTypeAdapter(LAnonymizeType.class, stringDeserializer);
        gsonBuilder.registerTypeAdapter(LUri.class, stringDeserializer);

        gson = gsonBuilder.create();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Serialize

    public static String serialize(InputType inputType) {
        if (inputType.getClass().isInstance(LString.class)) {
            return serialize((LString) inputType);
        }
        if (inputType.getClass().isInstance(LHash.class)) {
            return serialize((LHash) inputType);
        }
        if (inputType.getClass().isInstance(LArray.class)) {
            return serialize((LArray) inputType);
        }
        if (inputType.getClass().isInstance(LBool.class)) {
            return serialize((LBool) inputType);
        }
        //omitted Timestamp
        //omitted URI
        //omitted Anonymize

        throw new JsonParseException(LogstashMessages.ERROR_INVALID_INPUT_TYPE_TO_SERIALIZE + inputType.getClass().toString());
    }

    public static String serialize(LString lString) {
        return gson.toJson(lString);
    }

    public static String serialize(LArray lArray) {
        return gson.toJson(lArray);
    }

    public static String serialize(LBool lBool) {
        return gson.toJson(lBool);
    }

    public static String serialize(LHash lHash) {
        return gson.toJson(lHash);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Deserialize

    public static InputType deserialize(JsonElement jsonElement, LType argumentType) {

        if (argumentType.equals(LType.LSTRING)) {
            return deserializeString(jsonElement);
        }
        if (argumentType.equals(LType.LHASH_MAP)) {
            return deserializeObject(jsonElement);
        }
        if (argumentType.equals(LType.LARRAY)) {
            return deserializeArray(jsonElement);
        }
        if (argumentType.equals(LType.LBOOL)) {
            return deserializeBool(jsonElement);
        }
        if (argumentType.equals(LType.LTIMESTAMP_ARRAY)) {
            return deserializeTimestampArray(jsonElement);
        }
        if (argumentType.equals(LType.LANONYMIZE_ALGO)) {
            return deserializeAnonymize(jsonElement);
        }
        if (argumentType.equals(LType.LURI)) {
            return deserializeUri(jsonElement);
        }
        throw new JsonParseException(LogstashMessages.ERROR_INVALID_INPUT_TYPE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private static LString deserializeString(JsonElement json) {
        return gson.fromJson(json, LString.class);
    }

    private static LUri deserializeUri(JsonElement json) {
        LString string = gson.fromJson(json, (Type) LString.class);
        return new LUri(string.getLString());
    }

    private static LBool deserializeBool(JsonElement json) {
        return gson.fromJson(json, LBool.class);
    }

    private static LArray deserializeArray(JsonElement json) {
        return gson.fromJson(json, LArray.class);
    }

    private static LHash deserializeObject(JsonElement json) {
        return gson.fromJson(json, LHash.class);
    }

    private static LAnonymizeType deserializeAnonymize(JsonElement json) {
        LString string = gson.fromJson(json, (Type) LString.class);
        if (string.isInitialised()){
            return new LAnonymizeType(AnonymizeAlgorithm.valueOf(string.getLString()));
        }
        return new LAnonymizeType();
    }

    private static LTimestampArray deserializeTimestampArray(JsonElement json) {
        return new LTimestampArray(gson.fromJson(json, LArray.class));
    }
}
