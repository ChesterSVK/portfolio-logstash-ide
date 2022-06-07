package cz.muni.ics.logstash.commands.roots;

import cz.muni.ics.logstash.commands.nodes.NodeHandler;
import cz.muni.ics.logstash.exception.LogstashMessages;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class RootHandlerImpl implements RootHandler {

    private final NodeHandler nodeHandler;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public RootHandlerImpl(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Root getRootInstance(RootEnum rootEnum, Collection<Node> nodes) {
        Root r = new RootCommandInstance(rootEnum);
        this.addItems(r, nodes);
        return r;
    }

    @Override
    public void addItem(Root r, Node node) {
        List<String> allowedParentNames =
                nodeHandler.getAllowedParentRoots(node).stream().map(RootEnum::getCommandName).collect(Collectors.toList());

        if (!allowedParentNames.contains(r.getCommandName())) {
            throw new IllegalArgumentException(
                    LogstashMessages.ERROR_FORBIDDEN_NODE_TYPE + node.getCommandEnum().getCommandName());
        }
        int index = r.indexOf(node);
        if (index != -1) {
            nodeHandler.addOptions(r.get(index), node);
        } else {
            r.add(node);
        }
    }

    @Override
    public void removeNode(Root r, Node node) {
        int index = r.indexOf(node);
        if (index == -1)
            return;

        Node n = r.get(index);
        node.forEach(item -> nodeHandler.removeLeaf(n, item));

        //If empty leafs delete whole node
        if (n.isEmpty()) {
            r.remove(n);
        }
    }

    @Override
    public void addItems(Root r, Collection<Node> node) {
        node.forEach(item -> this.addItem(r, item));
    }
}
