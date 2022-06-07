package cz.muni.ics.logstash.commands.leafs;

import cz.muni.ics.logstash.commands.leafs.categories.*;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.exception.LogstashMessages;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import org.apache.commons.collections4.ListUtils;

import java.util.*;

public abstract class GenericLeafCommand implements CommandPattern {

    private String commandName;
    private LType commandType;
    private boolean required;
    private List<NodeEnum> allowedNodeParents;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    protected GenericLeafCommand(
            String commandName, LType commandType, List<NodeEnum> allowedNodeParents, boolean required) {

        this.commandName = commandName;
        this.commandType = commandType;
        this.allowedNodeParents = allowedNodeParents;
        this.required = required;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public String getCommandName() {
        return this.commandName;
    }

    @Override
    public LType getArgumentInputType() {
        return this.commandType;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    @Override
    public Collection<NodeEnum> getAllowedParentNodes() {
        return Collections.unmodifiableCollection(this.allowedNodeParents);
    }

    @Override
    public abstract Leaf getCommandInstance() throws IllegalAccessException, InstantiationException;

    @Override
    public boolean equalsCommand(String commandName, LType commandType) {
        return this.getCommandName().equals(commandName) && this.getArgumentInputType().equals(commandType);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public <T extends GenericLeafCommand> Leaf getCommandInstance(T command) throws InstantiationException, IllegalAccessException {
        return new LeafCommandInstance(command.getCommandName(), command.getArgumentInputType().getTypeInstance(),command.isRequired(), command.getAllowedParentNodes());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    //List all implemented leaves to be available
    public static <T extends GenericLeafCommand> List<T> values() {
        List<List<? extends GenericLeafCommand>> allCommands = Arrays.asList(
                CommonsLeaf.values(),
                MutateLeaf.values(),
                FingerprintLeaf.values(),
                ElasticsearchLeaf.values(),
                JsonLeaf.values(),
                RubyLeaf.values(),
                DateLeaf.values()
        );

        List<T> commands = new ArrayList<>();
        for (List l : allCommands) {
            commands.addAll(l);
        }
        return commands;
    }

    public static CommandPattern valueOf(String commandName, LType commandType) {
        for (GenericLeafCommand c : values()) {
            if (c.equalsCommand(commandName, commandType)) {
                return c;
            }
        }
        throw new IllegalArgumentException(LogstashMessages.ERROR_PARSING_LEAF + commandName + '.' + commandType);
    }
}
