package cz.muni.ics.logstash.commands.nodes;

import cz.muni.ics.logstash.commands.nodes.plugins.DnsResolvingCommand;
import cz.muni.ics.logstash.commands.nodes.plugins.FingerprintCommand;
import cz.muni.ics.logstash.commands.nodes.plugins.TimestampCommand;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.exception.LogstashMessages;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.interfaces.RootEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum NodeEnumImpl implements NodeEnum {

    PATH("path",
            Collections.singletonList(RootEnumImpl.INPUT), NodeCommandInstance.class),
    STDIN("stdin",
            Collections.singletonList(RootEnumImpl.INPUT), NodeCommandInstance.class),
    FILE("file",
            Collections.singletonList(RootEnumImpl.INPUT), NodeCommandInstance.class),
    MUTATE("mutate",
            Collections.singletonList(RootEnumImpl.FILTER), NodeCommandInstance.class),
    JSON("json",
            Collections.singletonList(RootEnumImpl.FILTER), NodeCommandInstance.class),
    RUBY("ruby",
            Collections.singletonList(RootEnumImpl.FILTER), NodeCommandInstance.class),
    ELASTICSEARCH("elasticsearch",
            Collections.singletonList(RootEnumImpl.OUTPUT), NodeCommandInstance.class),
    DATE("date",
            Collections.singletonList(RootEnumImpl.FILTER), NodeCommandInstance.class),
    FINGERPRINT("fingerprint",
            Collections.singletonList(RootEnumImpl.FILTER), NodeCommandInstance.class),
    ANONYMIZE("anonymize",
            Collections.singletonList(RootEnumImpl.FILTER), FingerprintCommand.class),
    TIMESTAMP("timestamp",
            Collections.singletonList(RootEnumImpl.FILTER), TimestampCommand.class),
    DNS("dns",
            Collections.singletonList(RootEnumImpl.FILTER), DnsResolvingCommand.class);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Variables

    private String commandName;
    private List<RootEnum> allowedRoots;
    private Class<Node> instanceClass;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    <T extends Node> NodeEnumImpl(String logstashCommandName, List<RootEnum> allowedRoots, Class<T> instanceClass) {
        this.commandName = logstashCommandName;
        this.allowedRoots = allowedRoots;
        this.instanceClass = (Class<Node>) instanceClass;
    }

    public static Node getNodeCommand(String value, List<Leaf> commandArguments) throws LogstashException {
        NodeEnumImpl command = NodeEnumImpl.valueOf(value.toUpperCase());
        return getNodeInstance(command, commandArguments);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public Collection<RootEnum> getAllowedParentRoots() {
        return Collections.unmodifiableList(allowedRoots);
    }

    @Override
    public String getCommandName() {
        return this.commandName;
    }

    @Override
    public Node getCommandInstance() throws LogstashException {
        return getNodeCommand(this.getCommandName(), Collections.emptyList());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private static Node getNodeInstance(NodeEnumImpl nodeEnum, List<Leaf> commandArguments) throws LogstashException {
        try {
            Node n = nodeEnum.instanceClass.newInstance();
            n.setCommandEnum(nodeEnum);
            n.addAll(commandArguments);
            return n;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LogstashException(LogstashMessages.ERROR_NODE_INSTANTIATION + e);
        }
    }
}
