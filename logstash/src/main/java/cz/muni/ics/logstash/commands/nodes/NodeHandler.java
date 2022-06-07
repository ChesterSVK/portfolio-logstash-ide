package cz.muni.ics.logstash.commands.nodes;

import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.*;

import java.util.Collection;
import java.util.List;

public interface NodeHandler {
    void addOption(Node node, Leaf leaf);
    Collection<RootEnum> getAllowedParentRoots(Node node);
    void removeLeaf(Node node, Leaf leaf);
    void addOptions(Node node, Collection<Leaf> leafs);
    void removeAllLeaves(Node node);
    Node getNodeInstance(Class<Node> instanceClass, NodeEnum nodeEnum, List<Leaf> commandArguments) throws LogstashException;
}
