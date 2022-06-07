package cz.muni.ics.logstash.commands.roots;

import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;

import java.util.Collection;
import java.util.List;

public interface RootHandler {
    Root getRootInstance(RootEnum rootEnum, Collection<Node> nodes);
    void addItem(Root r, Node item);
    void removeNode(Root r, Node node);
    void addItems(Root r, Collection<Node> node);
}
