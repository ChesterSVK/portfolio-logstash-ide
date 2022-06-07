package cz.muni.ics.logstash.serialization.leaves;

import com.google.gson.*;
import cz.muni.ics.logstash.commands.leafs.CommandPattern;
import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.serialization.types.InputTypeGsonHelper;

import java.lang.reflect.Type;

public class LeafDeserializer implements JsonDeserializer<Leaf> {

    @Override
    public Leaf deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String commandEnum = jsonObject.get(LeafCommandGsonHelper.commandEnum).getAsString();
        LType inputType = LType.valueOf(jsonObject.get(LeafCommandGsonHelper.commandType).getAsString());
        CommandPattern command = GenericLeafCommand.valueOf(commandEnum, inputType);
        return createLeaf(command, jsonObject);
    }

    private Leaf createLeaf(CommandPattern command, JsonObject jsonObject) {
        Leaf leaf = instantiateLeaf(command);
        if (jsonObject.get(LeafCommandGsonHelper.commandArgument) != null) {
            InputType commandArgument = InputTypeGsonHelper.deserialize(
                    jsonObject.get(LeafCommandGsonHelper.commandArgument), command.getArgumentInputType());

            leaf.setCommandArgument(commandArgument);
        }
        return leaf;
    }

    private Leaf instantiateLeaf(CommandPattern command) {
        try {
            return command.getCommandInstance();
        } catch (InstantiationException | IllegalAccessException e) { throw new JsonParseException(e); }
    }
}
