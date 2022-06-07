package cz.muni.ics.logstash.interfaces;

import cz.muni.ics.logstash.exception.LogstashException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface NodeEnum {
    Collection<RootEnum> getAllowedParentRoots();
    String getCommandName();
    Node getCommandInstance() throws LogstashException;
}
