package cz.muni.ics.logstash.serialization.nodes;

import com.google.gson.*;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.serialization.leaves.LeafCommandGsonHelper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NodeDeserializer implements JsonDeserializer<Node> {

    @Override
    public Node deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String commandEnum = jsonObject.get(NodeCommandGsonHelper.commandEnum).getAsString();
        List<Leaf> commandArgument = new ArrayList<>();
        if (jsonObject.get(NodeCommandGsonHelper.commandArgument) != null){
            JsonArray jsonArray = jsonObject.get(NodeCommandGsonHelper.commandArgument).getAsJsonArray();
            for (JsonElement e : jsonArray) { commandArgument.add(LeafCommandGsonHelper.deserialize(e.toString())); }
        }
        try {
            return NodeEnumImpl.getNodeCommand(commandEnum, commandArgument);
        } catch (LogstashException e) {
            log.error(String.valueOf(e));
            throw new JsonParseException(e);
        }
    }
}
