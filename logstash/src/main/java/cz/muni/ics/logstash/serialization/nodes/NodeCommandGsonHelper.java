package cz.muni.ics.logstash.serialization.nodes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import cz.muni.ics.logstash.commands.nodes.NodeCommandInstance;
import cz.muni.ics.logstash.commands.nodes.plugins.DnsResolvingCommand;
import cz.muni.ics.logstash.commands.nodes.plugins.FingerprintCommand;
import cz.muni.ics.logstash.commands.nodes.plugins.TimestampCommand;
import cz.muni.ics.logstash.interfaces.Node;
import org.springframework.stereotype.Component;

public abstract class NodeCommandGsonHelper {

    public static final String commandEnum = "commandEnum";
    public static final String commandArgument = "commandArgument";
    private static Gson gson;

    private static JsonSerializer<Node> nodeSerializer = new NodeSerializer();
    private static JsonDeserializer<Node> nodeDeserializer = new NodeDeserializer();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    static {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(NodeCommandInstance.class, nodeSerializer);
        gsonBuilder.registerTypeAdapter(TimestampCommand.class, nodeSerializer);
        gsonBuilder.registerTypeAdapter(DnsResolvingCommand.class, nodeSerializer);
        gsonBuilder.registerTypeAdapter(FingerprintCommand.class, nodeSerializer);

        gsonBuilder.registerTypeAdapter(NodeCommandInstance.class, nodeDeserializer);


        gson = gsonBuilder.create();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Serialize

    public static String serialize(Node node) {
        return gson.toJson(node);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Deserialize

    public static Node deserialize(String node) {
        return gson.fromJson(node, NodeCommandInstance.class);
    }
}
