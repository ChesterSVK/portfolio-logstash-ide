package cz.muni.ics.logstash.commands.roots;

import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;
import cz.muni.ics.logstash.serialization.roots.RootCommandGsonHelper;
import cz.muni.ics.logstash.utils.LogstashCommandPrettyPrinter;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class RootCommandInstance extends ArrayList<Node> implements Root {

    @EqualsAndHashCode.Include
    private RootEnum commandEnum;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    RootCommandInstance(RootEnum rootEnum) {
        this.commandEnum = rootEnum;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public String getCommandName() {
        return this.commandEnum.getCommandName();
    }

    @Override
    public List<Node> getCommandArgument() {
        return Collections.unmodifiableList(this);
    }

    @Override
    public String toJsonString() {
        return RootCommandGsonHelper.serialize(this);
    }

    @Override
    public String toLogstashString(int level) {
        return LogstashCommandPrettyPrinter.prettyPrintRoot(level, this.commandEnum.getCommandName(), this);
    }
}
