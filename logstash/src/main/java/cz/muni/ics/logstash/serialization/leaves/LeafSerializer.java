package cz.muni.ics.logstash.serialization.leaves;

import com.google.gson.*;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.lang.reflect.Type;

public class LeafSerializer implements JsonSerializer<Leaf> {

    @Override
    public JsonElement serialize(Leaf leaf, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonLeaf = new JsonObject();
        jsonLeaf.addProperty(LeafCommandGsonHelper.commandEnum, leaf.getCommandName());
        jsonLeaf.addProperty(LeafCommandGsonHelper.commandType, leaf.getCommandArgumentInputType().name());
        if (leaf.getCommandArgument() != null) {
            jsonLeaf.add(
                    LeafCommandGsonHelper.commandArgument,
                    new JsonParser().parse(leaf.getCommandArgument().toJsonString()));
        }
        return jsonLeaf;
    }
}
