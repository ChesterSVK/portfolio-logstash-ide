package cz.muni.ics.logstash.commands.leafs;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.NodeEnum;

import java.util.Collection;
import java.util.List;

public interface CommandPattern  {
    String getCommandName();
    LType getArgumentInputType();
    boolean isRequired();
    Collection<NodeEnum> getAllowedParentNodes();
    Leaf getCommandInstance() throws InstantiationException, IllegalAccessException;
    boolean equalsCommand(String commandName, LType commandType);
}
