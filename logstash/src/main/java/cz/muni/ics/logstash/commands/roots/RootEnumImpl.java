package cz.muni.ics.logstash.commands.roots;

import cz.muni.ics.logstash.commands.nodes.NodeHandlerImpl;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;

import java.util.Collections;
import java.util.List;

public enum RootEnumImpl implements RootEnum {

    INPUT("input"),
    OUTPUT("output"),
    FILTER("filter");

    private String commandName;
    private static RootHandler handler = new RootHandlerImpl(new NodeHandlerImpl());

    RootEnumImpl(String logstashRoot) {
        this.commandName = logstashRoot;
    }

    public static Root getRootCommand(String value, List<Node> commandArguments) {
        RootEnumImpl command = RootEnumImpl.valueOf(value.toUpperCase());
        return handler.getRootInstance(command, commandArguments);
    }

    @Override
    public String getCommandName() {
        return this.commandName;
    }

    @Override
    public Root getCommandInstance() {
        return getRootCommand(this.getCommandName(), Collections.emptyList());
    }
}
