package cz.muni.ics.logstash.commands.leafs;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.serialization.leaves.LeafCommandGsonHelper;
import cz.muni.ics.logstash.utils.LogstashCommandPrettyPrinter;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Objects;

@Data
public class LeafCommandInstance implements Leaf {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Variables

    @NonNull
    @NotEmpty
    private String commandName;
    @NonNull
    private LType commandArgumentInputType; //Used in web
    @NonNull
    private InputType commandArgument;
    private boolean required = false;  //Used in web
    @NonNull
    private Collection<NodeEnum> allowedParentNodes;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public LeafCommandInstance(
            @NonNull @NotEmpty
                    String commandName,
            @NonNull
                    InputType commandArgument,
            boolean required,
            @NonNull
                    Collection<NodeEnum> allowedParentNodes) {

        this.commandName = commandName;
        this.commandArgument = commandArgument;
        this.commandArgumentInputType = commandArgument.getType();
        this.required = required;
        this.allowedParentNodes = allowedParentNodes;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public String toJsonString() {
        return LeafCommandGsonHelper.serialize(this);
    }

    @Override
    public String toLogstashString(int level) {
        return LogstashCommandPrettyPrinter.prettyPrintLeaf(level, normalizeCommandName(this.commandName), this.commandArgument);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!(o instanceof LeafCommandInstance)) return false;
        LeafCommandInstance that = (LeafCommandInstance) o;
        return Objects.equals(commandName, that.getCommandName()) &&
                commandArgumentInputType == that.commandArgumentInputType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName, commandArgumentInputType);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private String normalizeCommandName(String commandEnum) {
        return commandEnum.split("#")[0];
    }
}
