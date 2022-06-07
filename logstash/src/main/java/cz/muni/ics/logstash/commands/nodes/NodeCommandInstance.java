package cz.muni.ics.logstash.commands.nodes;

import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.interfaces.RootEnum;
import cz.muni.ics.logstash.serialization.nodes.NodeCommandGsonHelper;
import cz.muni.ics.logstash.utils.LogstashCommandPrettyPrinter;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
public class NodeCommandInstance extends ArrayList<Leaf> implements Node {

    @Getter
    @Setter(AccessLevel.PUBLIC)
    @EqualsAndHashCode.Include
    private NodeEnum commandEnum;

    public NodeCommandInstance(){
        super();
    }

    @Override
    public String getCommandName() {
        return this.commandEnum.getCommandName();
    }

    @Override
    public String toJsonString() {
        return NodeCommandGsonHelper.serialize(this);
    }

    @Override
    public String toLogstashString(int level) {
        return LogstashCommandPrettyPrinter.prettyPrintNode(level, commandEnum.getCommandName(), this);
    }

    @Override
    public Collection<RootEnum> getAllowedRootCommands() {
        return commandEnum.getAllowedParentRoots();
    }
}
