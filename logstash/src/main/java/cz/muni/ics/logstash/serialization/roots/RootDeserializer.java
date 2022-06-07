package cz.muni.ics.logstash.serialization.roots;

import com.google.gson.*;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.serialization.nodes.NodeCommandGsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RootDeserializer implements JsonDeserializer<Root> {

    @Override
    public Root deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String commandEnum = jsonObject.get(RootCommandGsonHelper.commandEnum).getAsString();
        List<Node> commandArgument = new ArrayList<>();
        if (jsonObject.get(RootCommandGsonHelper.commandArgument) != null) {
            JsonArray jsonArray = jsonObject.get(RootCommandGsonHelper.commandArgument).getAsJsonArray();
            for (JsonElement o : jsonArray){ commandArgument.add(NodeCommandGsonHelper.deserialize(o.toString())); }
        }
        return RootEnumImpl.getRootCommand(commandEnum, commandArgument);
    }
}
