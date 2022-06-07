package cz.muni.ics.logstash.serialization.roots;

import com.google.gson.*;
import cz.muni.ics.logstash.interfaces.Root;

import java.lang.reflect.Type;

public class RootSerializer implements JsonSerializer<Root> {

    @Override
    public JsonElement serialize(Root root, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonNodeArg = new JsonArray();
        root.getCommandArgument().forEach(item -> {
            jsonNodeArg.add(new JsonParser().parse(item.toJsonString()));
        });
        JsonObject jsonNode = new JsonObject();
        jsonNode.addProperty(RootCommandGsonHelper.commandEnum, root.getCommandName());
        jsonNode.add(RootCommandGsonHelper.commandArgument, jsonNodeArg);
        return jsonNode;
    }
}
