package cz.muni.ics.logstash.commands.nodes;

import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.exception.LogstashMessages;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.interfaces.RootEnum;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public final class NodeHandlerImpl implements NodeHandler {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public void addOption(Node node, Leaf leaf) {
        if (!leaf.getAllowedParentNodes().contains(node.getCommandEnum())) {
            throw new IllegalArgumentException(LogstashMessages.ERROR_INVALID_LEAF_COMMAND + leaf.getCommandArgumentInputType());
        }
        if (!leaf.getCommandArgument().isInitialised()){
            return;
        }
        node.remove(leaf);
        node.add(leaf);
    }

    @Override
    public void removeLeaf(Node node, Leaf leaf) {
        Leaf leafToDelete = null;
        for (Leaf l : node) {
            if (l.equals(leaf)) {
                leafToDelete = l;
            }
        }
        if (leafToDelete != null) {
            node.remove(leafToDelete);
        }
    }

    @Override
    public void addOptions(Node node, Collection<Leaf> leafs) {
        leafs.forEach(item -> this.addOption(node, item));
    }

    @Override
    public void removeAllLeaves(Node node) {
        node.clear();
    }

    @Override
    public Node getNodeInstance(Class<Node> instanceClass, NodeEnum nodeEnum, List<Leaf> commandArguments) throws LogstashException {
        Node n;
        try {
            n = instanceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LogstashException(LogstashMessages.ERROR_NODE_INSTANTIATION, e);
        }
        n.setCommandEnum(nodeEnum);
        this.addOptions(n, commandArguments);
        return n;
    }

    @Override
    public Collection<RootEnum> getAllowedParentRoots(Node node) {
        return node.getCommandEnum().getAllowedParentRoots();
    }
}
