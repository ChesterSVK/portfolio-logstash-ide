package cz.muni.ics.logstash.serialization.roots;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import cz.muni.ics.logstash.commands.roots.RootCommandInstance;
import cz.muni.ics.logstash.interfaces.Root;
import org.springframework.stereotype.Component;

public abstract class RootCommandGsonHelper {

    public static final String commandEnum = "commandEnum";
    public static final String commandArgument = "commandArgument";
    private static Gson gson;

    private static JsonSerializer<Root> rootSerializer = new RootSerializer();
    private static JsonDeserializer<Root> rootDeserializer = new RootDeserializer();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    static  {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(RootCommandInstance.class, rootSerializer);
        gsonBuilder.registerTypeAdapter(RootCommandInstance.class, rootDeserializer);

        gson = gsonBuilder.create();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Serialize

    public static String serialize(Root root) {
        return gson.toJson(root);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Deserialize

    public static Root deserialize(String root) {
        return gson.fromJson(root, RootCommandInstance.class);
    }
}
