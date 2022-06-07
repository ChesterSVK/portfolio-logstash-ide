package cz.muni.ics.logstash.serialization.leaves;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import cz.muni.ics.logstash.commands.leafs.LeafCommandInstance;
import cz.muni.ics.logstash.interfaces.Leaf;
import org.springframework.stereotype.Component;

public abstract class LeafCommandGsonHelper {

    private static Gson gson;
    public static final String commandType = "commandInputType";
    public static final String commandEnum = "commandEnum";
    public static final String commandArgument = "commandArgument";

    private static JsonSerializer<Leaf> leafSerializer = new LeafSerializer();
    private static JsonDeserializer<Leaf> leafDeserializer = new LeafDeserializer();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    static  {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LeafCommandInstance.class, leafSerializer);
        gsonBuilder.registerTypeAdapter(LeafCommandInstance.class, leafDeserializer);

        gson = gsonBuilder.create();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Serialize

    public static String serialize(Leaf leaf) {
        return gson.toJson(leaf);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Deserialize

    public static Leaf deserialize(String leaf) {
        return gson.fromJson(leaf, LeafCommandInstance.class);
    }
}
