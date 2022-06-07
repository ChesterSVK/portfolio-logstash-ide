package cz.muni.ics.logstash.commands.roots;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.nodes.NodeHandlerImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class RootHandlerTest {

    private RootHandler handler = new RootHandlerImpl(new NodeHandlerImpl());
    private List<NodeEnum> testNodes;

    public RootHandlerTest() {
        testNodes = new ArrayList<>();
        for (NodeEnum nodeEnum : NodeEnumImpl.values()) {
            List<NodeEnum> excludedNodes = Arrays.asList(NodeEnumImpl.ANONYMIZE, NodeEnumImpl.TIMESTAMP, NodeEnumImpl.DNS);
            if (!excludedNodes.contains(nodeEnum)) {
                testNodes.add(nodeEnum);
            }
        }
    }

    @Test
    public void testCreateAllCommands() throws LogstashException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    handler.getRootInstance(rEnum, Collections.singleton(nEnum.getCommandInstance()));
                }
            }
        }
    }

    @Test
    public void testCreateAllCommandsInvalidNodes() {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (!nEnum.getAllowedParentRoots().contains(rEnum)) {
                    assertThatThrownBy(() -> handler.getRootInstance(rEnum, Collections.singleton(nEnum.getCommandInstance()))).isInstanceOf(IllegalArgumentException.class);
                }
            }
        }
    }

    @Test
    public void testAddItemAllCommands() throws LogstashException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();
                    handler.addItem(r, nEnum.getCommandInstance());
                    assertThat(r.getCommandArgument().size()).isEqualTo(1);
                }
            }
        }
    }

//    @Test
//    public void testAddExistingItemAllCommands() throws LogstashException, InstantiationException, IllegalAccessException {
//        for (RootEnum rEnum : RootEnumImpl.values()) {
//            for (NodeEnum nEnum : testNodes) {
//                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
//                    Root r = rEnum.getCommandInstance();
//                    Node n = nEnum.getCommandInstance();
//                    handler.addItem(r, n);
//                    assertThat(r.getCommandArgument().size()).isEqualTo(1);
//                    n.add(CommonsLeaf.ID.getCommandInstance());
//                    handler.addItem(r, n);
//                    assertThat(r.getCommandArgument().size()).isEqualTo(1);
//                }
//            }
//        }
//    }

    @Test
    public void testAddForbiddenItemAllCommands() {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (!nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();
                    assertThatThrownBy(() -> handler.addItem(r, nEnum.getCommandInstance())).isInstanceOf(IllegalArgumentException.class);
                }
            }
        }
    }

    @Test
    public void testAddItemsAllCommands() throws LogstashException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();
                    handler.addItems(r, Collections.singleton(nEnum.getCommandInstance()));
                    assertThat(r.getCommandArgument().size()).isEqualTo(1);
                }
            }
        }
    }

    @Test
    public void testAddForbiddenItemsAllCommands() {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (!nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();
                    assertThatThrownBy(() -> handler.addItems(r, Collections.singleton(nEnum.getCommandInstance()))).isInstanceOf(IllegalArgumentException.class);
                }
            }
        }
    }

    @Test
    public void testRemoveDeepItemWithNoConjuctionItemsAllCommands() throws LogstashException, InstantiationException, IllegalAccessException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();

                    Node n1 = nEnum.getCommandInstance();
                    n1.add(CommonsLeaf.ID.getCommandInstance());
                    assertThat(n1.size()).isEqualTo(1);

                    Node n2 = nEnum.getCommandInstance();
                    handler.addItem(r, n1);
                    handler.removeNode(r, n2);
                    assertThat(r.getCommandArgument().size()).isEqualTo(1);
                }
            }
        }
    }

    @Test
    public void testRemoveItemWithOneConjuctionItemAllCommands() throws LogstashException, InstantiationException, IllegalAccessException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();

                    Node n1 = nEnum.getCommandInstance();
                    n1.add(CommonsLeaf.ID.getCommandInstance());
                    assertThat(n1.size()).isEqualTo(1);

                    Node n2 = nEnum.getCommandInstance();
                    n2.add(CommonsLeaf.ID.getCommandInstance());
                    assertThat(n2.size()).isEqualTo(1);

                    handler.addItem(r, n1);
                    handler.removeNode(r, n2);
                    assertThat(r.getCommandArgument().size()).isEqualTo(0);
                }
            }
        }
    }


    @Test
    public void testRemoveNonExistingItemAllCommands() throws LogstashException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();
                    assertThat(r.getCommandArgument().size()).isEqualTo(0);
                    handler.removeNode(r, nEnum.getCommandInstance());
                    assertThat(r.getCommandArgument().size()).isEqualTo(0);
                }
            }
        }
    }

    @Test
    public void testRemoveNonExistingAllCommands() throws LogstashException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();

                    Node n2 = nEnum.getCommandInstance();
                    handler.removeNode(r, n2);
                    assertThat(r.getCommandArgument().size()).isEqualTo(0);
                }
            }
        }
    }

    @Test
    public void testRemoveAndClearAllCommands() throws LogstashException, InstantiationException, IllegalAccessException {
        for (RootEnum rEnum : RootEnumImpl.values()) {
            for (NodeEnum nEnum : testNodes) {
                if (nEnum.getAllowedParentRoots().contains(rEnum)) {
                    Root r = rEnum.getCommandInstance();
                    Node n = nEnum.getCommandInstance();
                    n.add(CommonsLeaf.ID.getCommandInstance());
                    handler.addItem(r, n);
                    assertThat(r.getCommandArgument().size()).isEqualTo(1);

                    Node n2 = nEnum.getCommandInstance();
                    n2.add(CommonsLeaf.ID.getCommandInstance());
                    handler.removeNode(r, n2);
                    assertThat(r.getCommandArgument().size()).isEqualTo(0);
                }
            }
        }
    }
}
