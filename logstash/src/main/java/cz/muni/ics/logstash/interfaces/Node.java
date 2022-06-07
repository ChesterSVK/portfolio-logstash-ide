package cz.muni.ics.logstash.interfaces;

import java.util.Collection;
import java.util.List;

public interface Node extends List<Leaf>, LogstashCommand {
    void setCommandEnum(NodeEnum commandEnum);
    Collection<RootEnum> getAllowedRootCommands();
    NodeEnum getCommandEnum();
}
