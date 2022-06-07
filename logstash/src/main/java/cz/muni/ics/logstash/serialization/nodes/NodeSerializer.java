package cz.muni.ics.logstash.serialization.nodes;

import com.google.gson.*;
import cz.muni.ics.logstash.interfaces.Node;

import java.lang.reflect.Type;

public class NodeSerializer implements JsonSerializer<Node> {

    @Override
    public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonNodeArg = new JsonArray();
        node.forEach(item -> {
            jsonNodeArg.add(new JsonParser().parse(item.toJsonString()));
        });
        JsonObject jsonNode = new JsonObject();
        jsonNode.addProperty(NodeCommandGsonHelper.commandEnum, node.getCommandName());
        jsonNode.add(NodeCommandGsonHelper.commandArgument, jsonNodeArg);
        return jsonNode;
    }
}
