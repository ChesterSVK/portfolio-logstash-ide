package cz.muni.ics.logstash.interfaces;

import cz.muni.ics.logstash.enums.LType;

import java.util.Collection;

public interface Leaf extends LogstashCommand {
    Collection<NodeEnum> getAllowedParentNodes();
    InputType getCommandArgument();
    void setCommandArgument(InputType argument);
    LType getCommandArgumentInputType();
}
